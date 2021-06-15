package pre_implemented;


import java.util.Random;

import com.xilinx.rapidwright.design.Design;
import com.xilinx.rapidwright.design.Module;
import com.xilinx.rapidwright.design.ModuleInst;
import com.xilinx.rapidwright.edif.EDIFCell;
import com.xilinx.rapidwright.edif.EDIFCellInst;
import com.xilinx.rapidwright.edif.EDIFDirection;
import com.xilinx.rapidwright.edif.EDIFNet;
import com.xilinx.rapidwright.edif.EDIFPort;
import com.xilinx.rapidwright.edif.EDIFPortInst;

import convolution.ConvolutionResources;
import keywords.DesignProperties;
import util.Utils;




public class Architecture {
	
	static Module conv1 = null;
	static ModuleInst conv1Inst = null;
	static Module iterator1 = null;
	static ModuleInst iterator1Inst = null;
	static Module pool1 = null;
	static ModuleInst pool1Inst = null;
	static Module relu1 = null;
	static ModuleInst relu1Inst = null;
	static Random rand = new Random();
	
	public static void connectIteratorConv(Design d1, EDIFCell topCell, EDIFCellInst conv, EDIFCellInst iterator,  EDIFNet clkNet,  EDIFNet rstNet) {

		
				clkNet.createPortInst("clk", conv);
				clkNet.createPortInst("clk", iterator);
				
				rstNet.createPortInst("rstn", conv);
				rstNet.createPortInst("rstn", iterator);
				
		for (int i = 1; i < 7; i++) {

				if(i==5 || i==6) {
					String netName = conv.getName() + "_"+ iterator.getName() + "_"
							+ Resoources.CONV_INPUT[i]+i;
					EDIFNet net = topCell.createNet(netName);
					net.createPortInst(Resoources.CONV_INPUT[i],  conv);
					net.createPortInst(Resoources.ITERATOR_OUTPUT[i], 1,  iterator);
				}
				else
					for (int j = 0; j < conv.getPort(Resoources.CONV_INPUT[j]).getWidth(); j++) {
						String netName = conv.getName() + "_"+ iterator.getName() + "_"
								+ Resoources.CONV_INPUT[i]+"_"+Resoources.ITERATOR_OUTPUT[i]+""+j+System.currentTimeMillis()+rand.nextLong();
						System.out.println("\ni="+i+"    Mapping j="+j);
						System.out.println("name="+netName);
						EDIFNet net = topCell.createNet(netName);
						net.createPortInst(Resoources.CONV_INPUT[i], j, conv);
						net.createPortInst(Resoources.ITERATOR_OUTPUT[i], j,  iterator);
					}
				
			}
	}
	
	public static void connectConvPool(Design d1, EDIFCell topCell, EDIFCellInst conv, EDIFCellInst pool,  EDIFNet clkNet,  EDIFNet rstNet) {

		
		clkNet.createPortInst("clk", conv);
		clkNet.createPortInst("clk", pool);
		
		rstNet.createPortInst("rstn", conv);
		rstNet.createPortInst("rstn", pool);
		

		for (int j = 0; j < conv.getPort(Resoources.CONV_OUTPUT[0]).getWidth(); j++) {
				String netName = conv.getName() + "_"+ pool.getName() + "_"
						+ Resoources.CONV_OUTPUT[0]+"_"+Resoources.POOLING_INPUT[1]+""+j+System.currentTimeMillis()+rand.nextLong();
				System.out.println("name="+netName);
				EDIFNet net = topCell.createNet(netName);
				net.createPortInst(Resoources.CONV_OUTPUT[0], j, conv);
				net.createPortInst(Resoources.POOLING_INPUT[1], j,  pool);
			}
		
}
	
public static void connectPoolRelu(Design d1, EDIFCell topCell, EDIFCellInst pool,  EDIFCellInst relu, EDIFNet clkNet,  EDIFNet rstNet) {

		
		clkNet.createPortInst("clk", relu);
		clkNet.createPortInst("clk", pool);
		
		rstNet.createPortInst("rstn", relu);
		rstNet.createPortInst("rstn", pool);
		
		for (int i = 1; i < 3; i++) {

			for (int j = 0; j < relu.getPort(Resoources.POOLING_OUTPUT[i]).getWidth(); j++) {
					String netName = relu.getName() + "_"+ pool.getName() + "_"
							+ Resoources.RELU_INPUT[i]+"_"+Resoources.POOLING_OUTPUT[i]+""+j+System.currentTimeMillis()+rand.nextLong();
					System.out.println("name="+netName);
					EDIFNet net = topCell.createNet(netName);
					net.createPortInst(Resoources.RELU_INPUT[i], j, relu);
					net.createPortInst(Resoources.POOLING_OUTPUT[i], j,  pool);
				}
		}
		
}
	
	public static void composition(Design d1){
		
		EDIFCell topCell = d1.getNetlist().getTopCell();
		EDIFNet clkNet = topCell.createNet("clk");
		EDIFNet rstNet = topCell.createNet("rstn");
		EDIFPort portClk = topCell.createPort("clk", EDIFDirection.INPUT, 1);
		EDIFPort portRst = topCell.createPort("rstn", EDIFDirection.INPUT, 1);
		//Ports and nets creation
		new EDIFPortInst(portClk, clkNet);
		new EDIFPortInst(portRst, rstNet);
		
		System.out.println(" Mapping C -> P");
		
		iterator1 = Utils.addNodeToDesign(d1, Resoources.ITERATOR, keywords.DCP_FOLDER_PATH, keywords.ITERATOR, 0, 0);
		iterator1Inst = d1.createModuleInst("iterator1" , iterator1);
		iterator1Inst.getCellInst().setCellType(iterator1.getNetlist().getTopCell());
		
		conv1 = Utils.addNodeToDesign(d1, Resoources.CONV, keywords.DCP_FOLDER_PATH, keywords.CONV, 0, 0);
		conv1Inst = d1.createModuleInst("conv1" , conv1);
		conv1Inst.getCellInst().setCellType(conv1.getNetlist().getTopCell());

		pool1 = Utils.addNodeToDesign(d1, Resoources.POOL, keywords.DCP_FOLDER_PATH, keywords.POOL1, 0, 0);
		pool1Inst = d1.createModuleInst("pool1" , pool1);
		pool1Inst.getCellInst().setCellType(pool1.getNetlist().getTopCell());
		

		relu1 = Utils.addNodeToDesign(d1, Resoources.relu, keywords.DCP_FOLDER_PATH, keywords.RELU1, 0, 0);
		relu1Inst = d1.createModuleInst("relu1" , relu1);
		relu1Inst.getCellInst().setCellType(relu1.getNetlist().getTopCell());
		
		
		connectIteratorConv(d1, topCell, conv1Inst.getCellInst(), iterator1Inst.getCellInst(),  clkNet,  rstNet);
		connectConvPool(d1, topCell, conv1Inst.getCellInst(), pool1Inst.getCellInst(),  clkNet,  rstNet);
		connectPoolRelu(d1, topCell, pool1Inst.getCellInst(), relu1Inst.getCellInst(),  clkNet,  rstNet);
		connectIteratorConv(d1, topCell, conv1Inst.getCellInst(), iterator1Inst.getCellInst(),  clkNet,  rstNet);
		connectConvPool(d1, topCell, conv1Inst.getCellInst(), pool1Inst.getCellInst(),  clkNet,  rstNet);
		connectPoolRelu(d1, topCell, pool1Inst.getCellInst(), relu1Inst.getCellInst(),  clkNet,  rstNet);connectIteratorConv(d1, topCell, conv1Inst.getCellInst(), iterator1Inst.getCellInst(),  clkNet,  rstNet);
		connectConvPool(d1, topCell, conv1Inst.getCellInst(), pool1Inst.getCellInst(),  clkNet,  rstNet);
		connectPoolRelu(d1, topCell, pool1Inst.getCellInst(), relu1Inst.getCellInst(),  clkNet,  rstNet);connectIteratorConv(d1, topCell, conv1Inst.getCellInst(), iterator1Inst.getCellInst(),  clkNet,  rstNet);
		connectConvPool(d1, topCell, conv1Inst.getCellInst(), pool1Inst.getCellInst(),  clkNet,  rstNet);
		connectPoolRelu(d1, topCell, pool1Inst.getCellInst(), relu1Inst.getCellInst(),  clkNet,  rstNet);
	}
	
	
	public static void main(String[] args) {
		Design d1 = new Design(keywords.DESIGN_NAME, keywords.DEVICE_NAME);
		d1.setAutoIOBuffers(false);
		
		
		try{
			d1.setDesignOutOfContext(true); //d1.setDesignAsOutOfContext();
			composition(d1);
			d1.getNetlist().exportEDIF(keywords.DCP_FOLDER_PATH+"result.edif");
			d1.writeCheckpoint(keywords.DCP_FOLDER_PATH+"result.dcp");
		}
		catch(Exception e) {
			e.printStackTrace();
	}

 }
}
