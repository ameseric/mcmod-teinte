package witherwar.utility;

import java.util.ArrayList;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3i;

public class VectorUtility {

	
	public static Vec3i scale( Vec3i v ,int i) {
		return new Vec3i( v.getX() * i ,v.getY()*i ,v.getZ()*i);
	}
	
	public static Vec3i multiply( Vec3i v ,Vec3i s) {
		return new Vec3i( v.getX() * s.getX() ,v.getY()*s.getY() ,v.getZ()*v.getZ());
	}
	
	
	public static ArrayList<EnumFacing> getPerpendicularFaces( EnumFacing face){
		ArrayList<EnumFacing> faces = new ArrayList<>();
		
		for( EnumFacing direction : EnumFacing.VALUES) {
			if( direction != face || direction != face.getOpposite()) {
				faces.add( direction);
			}
		}
		return faces;
	}
	
	
}
