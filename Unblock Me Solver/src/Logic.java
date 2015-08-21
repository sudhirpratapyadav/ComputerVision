import java.util.ArrayList;

public class Logic {
    
    private class Move{
        int i1,j1,i2,j2;
        public Move(int i1, int j1, int i2, int j2) {
            this.i1 = i1;
            this.j1 = j1;
            this.i2 = i2;
            this.j2 = j2;
        }
        
        public boolean equals(Move move)
        {
            return (move.i1==i1 && move.j1 == j1 && move.i2 == i2 && move.j2 ==j2);
        }
    }
    
    private class Position{
        int i,j;
        public Position(int i, int j) {
            this.i = i;
            this.j = j;
        }
    }
    
    private class MyHashMap{
        ArrayList<int [][]> states;
        ArrayList<Move> moves;
        
        public MyHashMap()
        {
            states = new ArrayList<>();
            moves = new ArrayList<>();
        }
        
        public void add(int[][] state,Move move)
        {
            states.add(state);
            moves.add(move);
        }
        
        public Move get(int[][] state)
        {
            Move move = new Move(-1, -1, -1, -1);
            boolean found = true;
            int i = 0;
            for (;i<states.size();i++) {
                found = true;
                int[][] temp = states.get(i);
                for (int j = 0; j < 6; j++) {
                    for (int k = 0; k < 6; k++) {
                        if(state[j][k]!=temp[j][k])
                        {
                            found = false;
                            break;
                        }
                        
                    }
                    if(!found)
                        break;
                }
                if(found)
                    break;
            }
            if(found)
            {
                move = moves.get(i);
            }
            return move;
        }
        
    }
    
    int[][] cur_state;
    int num;
    
    int no_visited;
    
    int[][][] visited_states;
    ArrayList<int [][]> myQeue;
    MyHashMap previousMoves;
    
    Position null_Postion;
    long lt=0;

    public Logic(int[][] state, int num) {
        cur_state = new int[6][6];
        for(int i = 0;i<6;i++)
        {
            System.arraycopy(state[i], 0, cur_state[i], 0, 6);
        }
        this.num = num;
        this.null_Postion = new Position(-1, -1);
        visited_states = new int[100000][6][6];
        no_visited=0;
        myQeue = new ArrayList<>();
        previousMoves = new MyHashMap();
    }
    
    public int[][][] getShortestPath()
    {
        myQeue.add(cur_state);
        Move hj = new Move(0, 0, 0, 0);
        previousMoves.add(cur_state, hj);
        int[][][] s_states = null;
        while(!myQeue.isEmpty())
        {
            int[][] state = new int[6][6];
            for(int i = 0;i<6;i++)
            {
                System.arraycopy(myQeue.get(0)[i], 0, state[i], 0, 6);
            }
            myQeue.remove(0);
            if(!isVisited(state))
            {
                for(int i = 0;i<6;i++)
                {
                    System.arraycopy(state[i], 0, visited_states[no_visited][i], 0, 6);
                }
                no_visited=no_visited+1;
                Position[] pos = getPositions(state);
                if(pos[0].j==4)
                {
                    ArrayList<int[][]> solution = new ArrayList();
                    boolean finding = true;
                    while(finding)
                    {
                        Move pm = previousMoves.get(state);
                        if(pm.equals(new Move(0, 0, 0, 0)))
                            finding = false;
                        state = getPreviousState(state,pm);
                        solution.add(state);
                    }
                    s_states = new int[solution.size()][6][6];
                    for (int i = 0; i <s_states.length; i++) {
                        for (int j = 0; j < 6; j++) {
                            System.arraycopy(solution.get(s_states.length-i-1)[j], 0, s_states[i][j], 0, 6);
                        }
                    }
                    return s_states;
                }
                Move[] moves = getNextMoves(state,pos);
                for (Move move : moves) {
                    int [][] st = getNextState(state, move);
                    previousMoves.add(st, move);
                    myQeue.add(st);
                }
            }
        }
        return s_states;
    }
    
    private boolean isSame(int[][] ar1,int[][] ar2)
    {
        for (int j = 0; j < 6; j++)
        {
            for (int k = 0; k < 6; k++)
            {
                if(ar1[j][k]!=ar2[j][k])
                    return false;
            }
        }
        return true;
    }
    
    private boolean isVisited(int[][] state)
    {
        for(int i=0;i<no_visited;i++)
        {
            if(isSame(visited_states[i], state)) {
                return true;
            }
        }
        return false;
    }
    
    
    
    private int[][] getNextState(int[][] temp_state,Move move){
        
        int[][] state = new int[temp_state.length][temp_state[0].length];
        for(int i=0;i<6;i++)
        {
            System.arraycopy(temp_state[i], 0, state[i], 0, 6);
        }
        if(move.i1==move.i2)
        {
            int val = state[move.i1][move.j1];
            if(move.j1+2<6 && val==state[move.i1][move.j1+2])
            {
                state[move.i1][move.j1]=0;
                state[move.i1][move.j1+1]=0;
                state[move.i1][move.j1+2]=0;
                state[move.i1][move.j2]=val;
                state[move.i1][move.j2+1]=val; 
                state[move.i1][move.j2+2]=val; 
           }
            else
            {
                state[move.i1][move.j1]=0;
                state[move.i1][move.j1+1]=0;
                state[move.i1][move.j2]=val;
                state[move.i1][move.j2+1]=val;
            }
        }
        else
        {
            
            int val = state[move.i1][move.j1];
            if(move.i1+2<6 && val==state[move.i1+2][move.j1])
            {
                state[move.i1][move.j1]=0;
                state[move.i1+1][move.j1]=0;
                state[move.i1+2][move.j1]=0;
                state[move.i2][move.j1]=val;
                state[move.i2+1][move.j1]=val; 
                state[move.i2+2][move.j1]=val; 
           }
            else
            {
                state[move.i1][move.j1]=0;
                state[move.i1+1][move.j1]=0;
                state[move.i2][move.j1]=val;
                state[move.i2+1][move.j1]=val;
            }
        }
        return state;
    }
    
    private int[][] getPreviousState(int[][] temp_state, Move move)
    {
        int[][] state = new int[temp_state.length][temp_state[0].length];
        int a = move.i1;
        move.i1 = move.i2;
        move.i2 = a;
        a = move.j1;
        move.j1 = move.j2;
        move.j2 = a;
        
        for(int i=0;i<6;i++)
        {
            System.arraycopy(temp_state[i], 0, state[i], 0, 6);
        }
        if(move.i1==move.i2)
        {
            int val = state[move.i1][move.j1];
            if(move.j1+2<6 && val==state[move.i1][move.j1+2])
            {
                state[move.i1][move.j1]=0;
                state[move.i1][move.j1+1]=0;
                state[move.i1][move.j1+2]=0;
                state[move.i1][move.j2]=val;
                state[move.i1][move.j2+1]=val; 
                state[move.i1][move.j2+2]=val; 
           }
            else
            {
                state[move.i1][move.j1]=0;
                state[move.i1][move.j1+1]=0;
                state[move.i1][move.j2]=val;
                state[move.i1][move.j2+1]=val;
            }
        }
        else
        {
            
            int val = state[move.i1][move.j1];
            if(move.i1+2<6 && val==state[move.i1+2][move.j1])
            {
                state[move.i1][move.j1]=0;
                state[move.i1+1][move.j1]=0;
                state[move.i1+2][move.j1]=0;
                state[move.i2][move.j1]=val;
                state[move.i2+1][move.j1]=val; 
                state[move.i2+2][move.j1]=val; 
           }
            else
            {
                state[move.i1][move.j1]=0;
                state[move.i1+1][move.j1]=0;
                state[move.i2][move.j1]=val;
                state[move.i2+1][move.j1]=val;
            }
        }
        return state;
    }
    
    private Position[] getPositions(int[][] state)
    {
        Position[] p = new Position[num];
        for (int i = 0; i < num; i++) {
           p[i] = null_Postion;    
        }        
        int n_r = state.length;
        int n_c = state[0].length;
        for(int i=0;i<n_r;i++)
        {
            for(int j=0;j<n_c;j++)
            {
                int ind = state[i][j];
                if(ind!=0)
                {
                    if(p[ind-1]==null_Postion)
                        p[ind-1]=new Position(i, j);
                }
            }
        }
        return p;
    }
    
    private Move[] getNextMoves(int[][] state,Position[] p){
        int n_r = state.length;
        int n_c = state[0].length;
        Move[] moves = new Move[50];
        int k=0;
        for (int ind = 0;ind < num; ind++) {
            int i1 = p[ind].i;
            int j1 = p[ind].j;
            if(i1==0)
            {
                if(j1==0)
                {
                    if(j1!=n_c-1 && state[i1][j1+1]==state[i1][j1])
                    {
                        int s=1;
                        for(int l=j1+2;l<n_c;l++)
                        {
                            if(state[i1][l]==state[i1][j1])
                            {
                                s=2;
                                continue;
                            }
                            if(state[i1][l]==0)
                            {
                                moves[k++] = new Move(i1, j1, i1, l-s);
                            }
                            else
                                break;
                        }
                    }
                    else
                    {
                        int s=1;
                        for(int l=i1+2;l<n_r;l++)
                        {
                            if(state[l][j1]==state[i1][j1])
                            {
                                s=2;
                                continue;
                            }
                            if(state[l][j1]==0)
                            {
                                moves[k++] = new Move(i1, j1, l-s, j1);
                            }
                            else
                                break;
                        }
                    }
                }
                else
                {
                    if(j1!=n_c-1 && state[i1][j1+1]==state[i1][j1])
                    {
                        int s=1;
                        for(int l=j1+2;l<n_c;l++)
                        {
                            if(state[i1][l]==state[i1][j1])
                            {
                                s=2;
                                continue;
                            }
                            if(state[i1][l]==0)
                            {
                                moves[k++] = new Move(i1, j1, i1, l-s);
                            }
                            else
                                break;
                        }
                        for(int l=j1-1;l>=0;l--)
                        {
                            if(state[i1][l]==0)
                            {
                                moves[k++] = new Move(i1, j1, i1, l);
                            }
                            else
                                break;
                        }
                    }
                    else
                    {
                        int s=1;
                        for(int l=i1+2;l<n_r;l++)
                        {
                            if(state[l][j1]==state[i1][j1])
                            {
                                s=2;
                                continue;
                            }
                            if(state[l][j1]==0)
                            {
                                moves[k++] = new Move(i1, j1, l-s, j1);
                            }
                            else
                                break;
                        }
                    }
                    
                }
            }
            else
            {
                if(j1==0)
                {
                    if(j1!=n_c-1 && state[i1][j1+1]==state[i1][j1])
                    {
                        int s=1;
                        for(int l=j1+2;l<n_c;l++)
                        {
                            if(state[i1][l]==state[i1][j1])
                            {
                                s=2;
                                continue;
                            }
                            if(state[i1][l]==0)
                            {
                                moves[k++] = new Move(i1, j1, i1, l-s);
                            }
                            else
                                break;
                        }
                    }
                    else
                    {
                        int s=1;
                        for(int l=i1+2;l<n_r;l++)
                        {
                            if(state[l][j1]==state[i1][j1])
                            {
                                s=2;
                                continue;
                            }
                            if(state[l][j1]==0)
                            {
                                moves[k++] = new Move(i1, j1, l-s, j1);
                            }
                            else
                                break;
                        }
                        for(int l=i1-1;l>=0;l--)
                        {
                            if(state[l][j1]==0)
                            {
                                moves[k++] = new Move(i1, j1, l, j1);
                            }
                            else
                                break;
                        }
                    }
                }
                else
                {
                    if(j1!=n_c-1 && state[i1][j1+1]==state[i1][j1])
                    {
                        int s=1;
                        for(int l=j1+2;l<n_c;l++)
                        {
                            if(state[i1][l]==state[i1][j1])
                            {
                                s=2;
                                continue;
                            }
                            if(state[i1][l]==0)
                            {
                                moves[k++] = new Move(i1, j1, i1, l-s);
                            }
                            else
                                break;
                        }
                        for(int l=j1-1;l>=0;l--)
                        {
                            if(state[i1][l]==0)
                            {
                                moves[k++] = new Move(i1, j1, i1, l);
                            }
                            else
                                break;
                        }
                    }
                    else
                    {
                        int s=1;
                        for(int l=i1+2;l<n_r;l++)
                        {
                            if(state[l][j1]==state[i1][j1])
                            {
                                s=2;
                                continue;
                            }
                            if(state[l][j1]==0)
                            {
                                moves[k++] = new Move(i1, j1, l-s, j1);
                            }
                            else
                                break;
                        }
                        for(int l=i1-1;l>=0;l--)
                        {
                            if(state[l][j1]==0)
                            {
                                moves[k++] = new Move(i1, j1, l, j1);
                            }
                            else
                                break;
                        }
                    }
                    
                }
            }
        }
        
        Move[] rmoves = new Move[k];
        System.arraycopy(moves, 0, rmoves, 0, k);
        return rmoves;
    }    
}
