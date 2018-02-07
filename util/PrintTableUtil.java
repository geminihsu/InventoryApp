package spirit.fitness.scanner.util;

import java.util.Arrays;
import java.util.List;

import spirit.fitness.string.tableview.Block;
import spirit.fitness.string.tableview.Board;
import spirit.fitness.string.tableview.Table;
public class PrintTableUtil {

	
	public static String printReport(List<String> headersList,List<List<String>> rowsList) 
	{
		
        Board board = new Board(100);
        Table table = new Table(board, 75, headersList, rowsList);
        table.setGridMode(Table.GRID_COLUMN);
        //setting width and data-align of columns
        List<Integer> colWidthsList = Arrays.asList(14, 14, 50, 14);
        List<Integer> colAlignList = Arrays.asList(Block.DATA_CENTER, Block.DATA_CENTER, Block.DATA_CENTER, Block.DATA_CENTER);
        table.setColWidthsList(colWidthsList);
        table.setColAlignsList(colAlignList);
        
        Block tableBlock = table.tableToBlocks();
        board.setInitialBlock(tableBlock);
        board.build();
        String tableString = board.getPreview();
        return tableString;
	}
	
	public static String printModelQuantityReport(List<String> headersList,List<List<String>> rowsList) 
	{
		
        Board board = new Board(200);
        Table table = new Table(board, 75, headersList, rowsList);
        table.setGridMode(Table.GRID_COLUMN);
        //setting width and data-align of columns
        List<Integer> colWidthsList = Arrays.asList(8, 20, 8, 12,10);
        List<Integer> colAlignList = Arrays.asList(Block.DATA_CENTER, Block.DATA_CENTER, Block.DATA_CENTER, Block.DATA_CENTER, Block.DATA_CENTER);
        table.setColWidthsList(colWidthsList);
        table.setColAlignsList(colAlignList);
        
        Block tableBlock = table.tableToBlocks();
        board.setInitialBlock(tableBlock);
        board.build();
        String tableString = board.getPreview();
        return tableString;
	}
	
	
	public static String noBorad(List<String> headersList,List<List<String>> rowsList) {
		
		   
		Board board = new Board(80);
		Table table = new Table(board, 80, headersList, rowsList);
        List<Integer> colWidthsList = Arrays.asList(14, 14, 30, 14);
        table.setColWidthsList(colWidthsList);
        table.invalidate().setGridMode(Table.GRID_NON).setRowsList(rowsList);
        String preview3 = board.invalidate().setInitialBlock(table.tableToBlocks()).build().getPreview();
        System.out.println("TABLE EXAMPLE 3");
        System.out.println(preview3);
        return preview3;
	}
}
