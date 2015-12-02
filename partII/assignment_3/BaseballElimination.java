import java.util.Hashtable;
import java.util.TreeMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.ArrayList;
import java.util.Stack;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;

public class BaseballElimination {
    // input data
    private Hashtable<String, Integer> teams;
    private String[] teamsName;
    private int[]    wins;
    private int[]    losses;
    private int[]    remainingGames;
    private int[]    gamesLeft;
    private int      size;
    // internal data
    private FlowNetwork network;
    private ArrayList<Integer> eliminatedTeams;
    // key wins, value team number
    private TreeMap<Integer, Integer> sortedBestTeams = new TreeMap<Integer, Integer>();
    private Stack<String> eliminatTeams;

    public BaseballElimination(String filename) {                    // create a baseball division from given filename in format specified below
        In file    = new In(filename);
        size       = file.readInt();
        teams      = new Hashtable<String, Integer>(size);
        teamsName  = new String[size];
        wins       = new int[size];
        losses     = new int[size];
        remainingGames  = new int[size];
        gamesLeft       = new int[size * size];

        // read input data        
        int i = 0;
        while (i != size) {
            teamsName[i] = file.readString();
            teams.put(teamsName[i], i);
            wins[i]           = file.readInt();
            sortedBestTeams.put(wins[i], i);
            losses[i]         = file.readInt();
            remainingGames[i] = file.readInt();
            for (int j = 0; j < size; ++j)
            {
                gamesLeft[i * size + j] = file.readInt();
            }
            ++i;
        }
    }
    
    public              int numberOfTeams() {                        // number of teams
        return size;
    }
    
    public Iterable<String> teams() {                                // all teams
        return teams.keySet();
    }
    
    public              int wins(String team) {                      // number of wins for given team
        return wins[checkTeams(team)];
    }
    
    public              int losses(String team) {                    // number of losses for given team
        return losses[checkTeams(team)];    
    }
    
    public              int remaining(String team) {                 // number of remaining games for given team
        return remainingGames[checkTeams(team)];
    }
    
    public              int against(String team1, String team2) {    // number of remaining games between team1 and team2
        int lhs = checkTeams(team1);
        int rhs = checkTeams(team2);
        return wins[lhs * size + rhs];
    }
    
    public          boolean isEliminated(String team) {              // is given team eliminated?
        int that = checkTeams(team);
        if (checkTrivialCase(that)) {
            return true;
        }
        checkNonTrivialCase(that);
        return false;
    }
    
    public Iterable<String> certificateOfElimination(String team) {  // subset R of teams that eliminates given team; null if not eliminated
        return eliminatTeams;
    }

    private int checkTeams(String team) {
        Integer i = teams.get(team);
        if (i == null) {
            throw new java.lang.IllegalArgumentException();
        }
        return i;
    }

    private boolean checkTrivialCase(int teamNumber) {
        NavigableMap<Integer, Integer> teamsIsBetter = sortedBestTeams.tailMap(wins[teamNumber] + remainingGames[teamNumber]
                                                        , false);
        eliminatTeams = new Stack<String>(); 
        if (teamsIsBetter.isEmpty()) {
            return false;
        }
        for (Map.Entry<Integer, Integer> entry: teamsIsBetter.entrySet())
        {
            eliminatTeams.push(teamsName[entry.getValue()]);
        }
        return true;
    }

    private boolean checkNonTrivialCase(int teamNumber) {
        int flowNetworkSize = ((size - 1) * (size - 2) / 2) + size - 1 + 2;
        network = new FlowNetwork(flowNetworkSize);
        int accumulator = 0;
        int i = 1;
        int teamCanWin = wins[teamNumber] + remainingGames[teamNumber];
        for (int pj = 0, j = 0; j < size; ++j, ++pj) {
            if (j == teamNumber) {
                --pj;
                continue;
            }

            for (int pz = j + 1, z = j + 1; z < size; ++z, ++pz) {
                if (z == teamNumber) {
                    --pz;
                    continue;
                }

                network.addEdge(new FlowEdge(0, i, gamesLeft[j * size + z]));
                accumulator += gamesLeft[j * size + z];
                network.addEdge(new FlowEdge(i, network.V() - size + pj, Integer.MAX_VALUE));
                network.addEdge(new FlowEdge(i, network.V() - size + pz, Integer.MAX_VALUE));
                ++i;
            }
            network.addEdge(new FlowEdge(network.V() - size + pj, network.V() - 1, teamCanWin - wins[j]));
        }
        FordFulkerson solve = new FordFulkerson(network, 0, network.V() - 1);
        System.out.printf("team %d: %f acc %d\n", teamNumber, solve.value(), accumulator);


        /*for (Map.Entry<Integer, Integer> entry: teamsIsBetter.entrySet())
        {
            eliminatTeams.push(teamsName[entry.getValue()]);
        }*/
        return false;
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}