package witherwar.utility;

import java.util.Random;




/**
 * 
 * @author https://stackoverflow.com/users/1995906/user1995906
 * 
 * taken from https://stackoverflow.com/questions/22752607/artifacts-using-midpoint-displacement-generation
 * with minor alterations
 *
 */
public class MidpointDisplacementNoise {

	
	
	private MidpointDisplacementNoise(){}
	
	//The size of the map will be 2^iterations + 1
	public static int[][] genMap(int iterations, double roughness){
	    Random rand = new Random();
	    return genMap(iterations, roughness, rand.nextLong());
	}
	
	
	
	public static int[][] genMap(int iterations, double roughness, long seed){
	    Random r = new Random(seed);
	    double rConstant = Math.pow(2,-roughness);
	    double currRoughVal = 1.0;
	
	    int size = (int)Math.pow(2,iterations); //size needs to be a power of 2 + 1 - this size includes 0
	    int subSize = size; //the "working" size of the squares
	    int stride = subSize/2;
	    boolean oddline = false; //not entirely sure what this does either...
	
	    int[][] map = new int[size + 1][size + 1];
	
	    //initalize corners
	    //int init = r.nextInt();
	    //int init = 0;
	    //map[0][0] = map[0][size] = map[size][0] = map[size][size] = init;
	    map[0][0] = r.nextInt();
	    map[0][size] = r.nextInt();
	    map[size][0] = r.nextInt();
	    map[size][size] = r.nextInt();
	
	    while(stride != 0){
	
	        //the square part
	        for(int x = stride; x < subSize; x += stride){
	            for(int y = stride; y < subSize; y += stride){
	                map[x][y] = (int)(r.nextInt() * currRoughVal) + avgSquareVals(x, y, stride, map);
	                y += stride;
	            }
	            x += stride;
	        }
	
	        //the diamond part
	        //not entirely sure what oddline is for, but it seems nessicary
	        oddline = false;
	        for (int x = 0; x < subSize; x += stride){
	            oddline = (oddline == false);
	            for (int y = 0; y < subSize; y += stride){
	                if (oddline && y == 0){y += stride;}
	
	                map[x][y] = (int)(r.nextInt() * currRoughVal) + avgDiamondVals(x, y, stride, size, subSize, map);
	
	                //this wraps the map - i'm ignoring this for now
	                //if(x == 0){}
	                //if(y == 0){}
	
	                y += stride;
	            }
	            //not sure x doesn't need to be incremented, but it doesn't?
	        }
	
	        //reduce range and halve stride
	        currRoughVal *= rConstant;
	        stride /= 2;
	    }
	
	    return map;
	}
	
	private static int avgSquareVals(int x, int y, int stride, int[][] map){
	    return (map[x + stride][y + stride] +
	            map[x + stride][y - stride] +
	            map[x - stride][y + stride] +
	            map[x - stride][y - stride])/4;
	}
	private static int avgDiamondVals(int x, int y, int stride, int size, int subSize, int[][] map){
	    if(x == 0){
	        return (map[x + stride][y] +
	                map[x][y + stride] +
	                map[x][y - stride])/3;
	    }
	    else if(x == size){
	        return (map[x - stride][y] +
	                map[x][y + stride] +
	                map[x][y - stride])/3;
	    }
	    else if(y == 0){
	        return (map[x + stride][y] +
	                map[x - stride][y] +
	                map[x][y + stride])/3;
	    }
	    else if( y == size){
	        return (map[x + stride][y] +
	                map[x - stride][y] +
	                map[x][y - stride])/3;
	    }
	    else{
	        return (map[x + stride][y] +
	                map[x - stride][y] +
	                map[x][y + stride] +
	                map[x][y - stride])/4;
	    }
	}
	
	
	
    public static void printAsCSV( int[][] map) {
        for(int i = 0;i < map.length;i++) {
            for(int j = 0;j < map[0].length;j++) {
                System.out.print(map[i][j]);
                System.out.print(",");
            }
            System.out.println();
        }
    }
	
	
}


