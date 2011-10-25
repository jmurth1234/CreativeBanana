package disabled.packages;

import org.bukkit.Chunk;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.WorldListener;

import com.ubempire.flatmap.FlatMap;

public class FMChunk extends WorldListener {
	@SuppressWarnings("unused")
	private final FlatMap plugin;
	public FMChunk(FlatMap callbackPlugin) {
		plugin = callbackPlugin;
	}
	
	public void onChunkLoad(ChunkLoadEvent event)
	{
		Chunk chunk = event.getChunk();
		
		if(!(chunk.getBlock(0, 0, 0).getTypeId()==89))
		//if(1==2)
		{
			System.out.println(chunk.getX()+","+chunk.getZ());
		for(int x=0; x<16; x++){
			for(int y=0; y<128; y++){
				for(int z=0; z<16; z++){
					if(x==0 && y==0 && z==0){chunk.getBlock(x, y, z).setTypeId(89);}
					else if(y<2){chunk.getBlock(x, y, z).setTypeId(7);}
					else if(y<65){chunk.getBlock(x, y, z).setTypeId(1);}
					else{chunk.getBlock(x, y, z).setTypeId(0);}
				}}}
				}
	}
	}
