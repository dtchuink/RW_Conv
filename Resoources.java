package pre_implemented;

public class Resoources {
	public static final String[] CONV_INPUT={/*node on top in the LinkBlaze topology*/
			"go", "cena_src", "qa_src", "qa_bias_conv1", "qa_weight_conv1", "first_data", "last_data", "aa_conv1_buf","cena_conv1_buf"
			
	};
	
	public static final String[] CONV_OUTPUT={/*node at the bottom in the LinkBlaze topology*/
			 "qa_conv1_buf"
	};	
	
	public static final String[] ITERATOR_INPUT={/*node at the bottom in the LinkBlaze topology*/
			"go",
	};	

	public static final String[] ITERATOR_OUTPUT={/*node on top in the LinkBlaze topology*/
			"", "cena_src", "aa_src" , "qa_bias_conv1", "qa_weight_conv1", "first_data_d", "last_data_d", "cena_src_d"
	};
	
		
	public static final String[] RELU_INPUT={/*node at the bottom in the LinkBlaze topology*/
			"go", "qa_pooling1_en", "qa_pooling1", "cena_relu1_buf", "aa_relu1_buf"
	};
	
	
	public static final String[] RELU_OUTPUT={/*node on top in the LinkBlaze topology*/
			"qa_relu1_buf"
	};
	
	public static final String[] POOLING_INPUT={/*node on top in the LinkBlaze topology*/
			"go", "qa_conv1_buf"
	};
		
	public static final String[] POOLING_OUTPUT={/*node at the bottom in the LinkBlaze topology*/
			"", "qa_pooling1_en",  "qa_pooling1", "aa_conv1_buf", "cena_conv1_buf", "pooling1_ready"
	};
	
	
	
	public static final String CONV = "conv1";
	public static final String POOL = "pool1";
	public static final String relu = "relu1";
	public static final String ITERATOR = "iterator1";
	public static final String CLK = "clk";
	public static final String RST = "rstn";

}