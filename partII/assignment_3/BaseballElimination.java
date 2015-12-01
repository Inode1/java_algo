import java.util.Hashtable;
import java.util.ArrayList;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;

public class BaseballElimination {
    // input data
    private Hashtable<String, Integer> teams;
    private int[] wins;
    private int[] losses;
    private int[] remainingGames;
    private int[] gamesLeft;
    private int   size;
    // max wins
    private int   maxWins = 0;
    // internal data
    private FlowNetwork network;
    private ArrayList<Integer> eliminatedTeams;

    public BaseballElimination(String filename) {                    // create a baseball division from given filename in format specified below
        In file    = new In(filename);
        size       = file.readInt();
        teams      = new Hashtable<String, Integer>(size);
        wins       = new int[size];
        losses     = new int[size];
        remainingGames  = new int[size];
        gamesLeft       = new int[size * size];

        // read input data        
        int i = 0;
        while (i != size) {
            teams.put(file.readString(), i);
            wins[i]           = file.readInt();
            if (wins[i] > maxWins) {
                maxWins = wins[i];
            }
            losses[i]         = file.readInt();
            remainingGames[i] = file.readInt();
            for (int j = 0; j < size; ++j)
            {
                gamesLeft[i * size + j] = file.readInt();
            }
            ++i;
        }
        eliminatedTeams = new ArrayList<Integer>(size);
        network = new FlowNetwork(size);



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
        return losses[checkTeams(team)];;    
    }
    
    public              int remaining(String team) {                 // number of remaining games for given team
        return remaining[checkTeams(team)];;
    }
    
    public              int against(String team1, String team2) {    // number of remaining games between team1 and team2
        int lhs = checkTeams(team1);
        int rhs = checkTeams(team2);
        return wins[lhs * size + rhs];;
    }
    
    public          boolean isEliminated(String team) {              // is given team eliminated?
        
        return true;
    }
    
    public Iterable<String> certificateOfElimination(String team) {  // subset R of teams that eliminates given team; null if not eliminated
        return null;
    }

    private int checkTeams(String team) {
        Integer i = teams.get(team);
        if (i == null) {
            throw new java.lang.IllegalArgumentException();
        }
        return i;
    }

    private void checkTrivialCase() {
        for (int i = 0; i < size; ++i) {
            if (wins[i] + remainingGames[i] < maxWins) {
                eliminatedTeams.add(i);
            }
        }
    }

    public static void main(String[] args) {
        BaseballElimination teams = new BaseballElimination(args[0]);
    }
}