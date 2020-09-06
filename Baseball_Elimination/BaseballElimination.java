import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class BaseballElimination {
    private final int[] wins, loss, remaining;
    private final int[][] games;
    private final Map<String, Integer> teams = new HashMap<>();
    private final Map<Integer, String> teamsById = new HashMap<>();
    private boolean elimination_status;
    private String selected_team;
    private List<String> certificateOfElimination;

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        In input = new In(filename);
        int teamsCount = input.readInt();
        wins = new int[teamsCount];
        loss = new int[teamsCount];
        remaining = new int[teamsCount];
        games = new int[teamsCount][teamsCount];

        for (int i = 0; i < teamsCount; i++) {
            String teamName = input.readString();
            teams.put(teamName, i);
            teamsById.put(i, teamName);
            wins[i] = input.readInt();
            loss[i] = input.readInt();
            remaining[i] = input.readInt();
            //games[i] = new int[teamsCount];
            for (int j = 0; j < teamsCount; j++) {
                games[i][j] = input.readInt();
            }
        }
    }


    public int numberOfTeams()                        // number of teams
    {
        return teams.size();
    }

    public Iterable<String> teams()                                // all teams
    {
        return teams.keySet();
    }

    public int wins(String team)                      // number of wins for given team
    {
        if (!teams.containsKey(team)) throw new IllegalArgumentException("Arg is null");
        return wins[teams.get(team)];
    }

    public int losses(String team)                    // number of losses for given team
    {
        if (!teams.containsKey(team)) throw new IllegalArgumentException("Arg is null");
        return loss[teams.get(team)];
    }

    public int remaining(String team)                 // number of remaining games for given team
    {
        if (!teams.containsKey(team)) throw new IllegalArgumentException("Arg is null");
        return remaining[teams.get(team)];
    }

    public int against(String team1, String team2)    // number of remaining games between team1 and team2
    {
        if (!teams.containsKey(team1) || !teams.containsKey(team2)) throw new IllegalArgumentException("Arg is null");
        return games[teams.get(team1)][teams.get(team2)];
    }

    public boolean isEliminated(String team)              // is given team eliminated?
    {
        if (!teams.containsKey(team)) throw new IllegalArgumentException("Arg is null");

        /*Case: Trivial */
        int maxWins = remaining(team) + wins(team);
        for (String otherTeam : teams()) {
            if (!otherTeam.equals(team) && wins(otherTeam) > maxWins) {
                return true;
            }
        }
        /*Case: Non-Trivial */
        if (selected_team != team) {
            isNonTriviallyEliminated(team);
        }
        return elimination_status;
    }

    public Iterable<String> certificateOfElimination(String team) {
        if (!teams.containsKey(team)) throw new IllegalArgumentException("Arg is null");
        if (selected_team != team) {
            isNonTriviallyEliminated(team);
        }
        return certificateOfElimination;
    }

    private void isNonTriviallyEliminated(String team) {
        selected_team = team;
        certificateOfElimination = null;

        int teamId = teams.get(team);
        int MaxWins = wins[teamId] + remaining[teamId];
        int gamesBetweenTeams = 0;

        for (int i = 0; i < numberOfTeams() - 1; i++) {
            for (int j = i + 1; j < numberOfTeams() - 1; j++) {
                if (i == teamId || j == teamId) {
                    continue;
                }
                gamesBetweenTeams++;
            }
        }
        int vertexCount = 2 + numberOfTeams() - 1 + gamesBetweenTeams;
        int source = 0, target = vertexCount - 1;
        int totalLeftGames = 0;
        int gameVertexId = 1;
        FlowNetwork fn = new FlowNetwork(vertexCount);

        for (int i = 0; i < numberOfTeams(); i++) {
            for (int j = i + 1; j < numberOfTeams(); j++) {
                if (i == teamId || j == teamId) {
                    continue;
                }
                fn.addEdge(new FlowEdge(source, gameVertexId, games[i][j]));
                totalLeftGames += games[i][j];
                fn.addEdge(new FlowEdge(gameVertexId, i + gamesBetweenTeams + 1, Double.MAX_VALUE));
                fn.addEdge(new FlowEdge(gameVertexId, j + gamesBetweenTeams + 1, Double.MAX_VALUE));
                gameVertexId++;
            }
        }
        for (int i = 0; i < numberOfTeams(); i++) {
            if (i == teamId) {
                continue;
            }
            fn.addEdge(new FlowEdge(i + gamesBetweenTeams + 1, target, MaxWins - wins[i] > 0 ? MaxWins - wins[i] : 0));
        }
        FordFulkerson ff = new FordFulkerson(fn, source, target);
        if (totalLeftGames > ff.value()) {
            certificateOfElimination = new LinkedList<>();
            for (int i = 0; i < numberOfTeams(); i++) {
                if (i == teamId) {
                    continue;
                }
                if (ff.inCut(i + gamesBetweenTeams + 1)) {
                    certificateOfElimination.add(teamsById.get(i));
                }
            }
            elimination_status = true;
            return;
        }
        elimination_status = false;
        return;
    }


}
