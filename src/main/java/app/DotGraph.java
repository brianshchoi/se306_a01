package app;

public class DotGraph {

    private static final String OUTPUT_PREFIX = "output";
    private String _title;

    public DotGraph(){
        this._title = OUTPUT_PREFIX;
    }

    DotGraph(final String title){
        // Capitalise first letter of graph name and add "output" prefix
        this._title = OUTPUT_PREFIX + title.substring(0, 1).toUpperCase() + title.substring(1);
    }

    public String getTitle() {
        return _title;
    }

    // This method generates a DOT file of the rendered optimal schedule
    public void render(){
        System.out.println(DotRenderer.openGraph("outputExample"));
    }
}
