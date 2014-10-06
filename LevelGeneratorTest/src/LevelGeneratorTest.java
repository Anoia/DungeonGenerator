import com.stuckinadrawer.FileReader;
import com.stuckinadrawer.graphs.Grammar;
import com.stuckinadrawer.graphs.GrammarManager;
import com.stuckinadrawer.graphs.Graph;

import java.io.IOException;

public class LevelGeneratorTest {

    private Graph levelGraph;
    Tile[][] level;

    private int levelWidth = 64;
    private int levelHeight = 64;

    public LevelGeneratorTest(){
        levelGraph = createLevelGraph();
        level = initLevel();

    }

    private Tile[][] initLevel() {
        Tile[][] level = new Tile[levelWidth][levelHeight];

        for (int x = 0; x < levelWidth; x++) {
            for (int y = 0; y < levelHeight; y++) {
                level[x][y] = Tile.EMPTY;
            }
        }
        return level;
    }

    private Graph createLevelGraph() {
        GrammarManager grammarManager = new GrammarManager();
        grammarManager.setGrammar(loadGrammar());
        grammarManager.applyAllProductions();
        return grammarManager.getCurrentGraph();
    }

    private Grammar loadGrammar(){
        FileReader fr = new FileReader();
        try {
            return fr.loadGrammar("grammar1.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Grammar();
    }

    public static void main(String [] arg){
        new LevelGeneratorTest();
    }
}
