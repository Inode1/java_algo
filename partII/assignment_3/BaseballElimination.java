import java.util.Hashtable;
import edu.princeton.cs.algs4.In;

public class BaseballElimination {
    private Hashtable<String, Integer> teams;
    private int[] wins;
    private int[] losses;
    private int[] remainingGames;
    private int[] gamesLeft;
    private int   size;

    public BaseballElimination(String filename) {                    // create a baseball division from given filename in format specified below
        In file    = new In(filename);
        size       = file.readInt();
        teams      = new Hashtable<String, Integer>(size);
        wins       = new int[size];
        losses     = new int[size];
        remainingGames  = new int[size];
        gamesLeft       = new int[size * size];

        String line;
        int i = 0;
        int tempSize = size;
        while (tempSize != 0) {
            teams.put(file.readString(), i);
            wins[i]           = file.readInt();
            losses[i]         = file.readInt();
            remainingGames[i] = file.readInt();
            for (int j = 0; j < size; ++j)
            {
                gamesLeft[i * size + j] = file.readInt();
            }
            ++i;
            --tempSize;
        }
    }
    
    public              int numberOfTeams() {                        // number of teams
        return 1;
    }
    
    public Iterable<String> teams() {                                // all teams
        return null;
    }
    
    public              int wins(String team) {                      // number of wins for given team
        return 1;
    }
    
    public              int losses(String team) {                    // number of losses for given team
        return 1;    
    }
    
    public              int remaining(String team) {                 // number of remaining games for given team
        return 1;
    }
    
    public              int against(String team1, String team2) {    // number of remaining games between team1 and team2
        return 1;
    }
    
    public          boolean isEliminated(String team) {              // is given team eliminated?
        return true;
    }
    
    public Iterable<String> certificateOfElimination(String team) {  // subset R of teams that eliminates given team; null if not eliminated
        return null;
    }
    
    public static void main(String[] args) {
        BaseballElimination teams = new BaseballElimination(args[0]);
    }
}