package app;

import taskModel.Task;

public class DotRenderer {

    private static final String NEWLINE = String.format("%n");

    private static final String TAB = "\t";

    protected DotRenderer(){
    }

    public static String openGraph(String title) {
        final StringBuffer sb = new StringBuffer();
        sb.append("digraph ");
        sb.append(title);
        sb.append(" {");
        sb.append(NEWLINE);

        return sb.toString();
    }

    public static String addNode(Task task, int startTime, int processor){
        final StringBuffer sb = new StringBuffer();
        sb.append(TAB);
        sb.append(task.getName());
        sb.append(TAB);
        sb.append("[Weight=");
        sb.append(task.getWeight());
        sb.append(",Start=");
        sb.append(startTime);
        sb.append(",Processor=");
        sb.append(processor);
        sb.append("];");
        sb.append(NEWLINE);

        return sb.toString();
    }

    public static String addDependency(Task parent, Task child){
        final StringBuffer sb = new StringBuffer();
        sb.append(TAB);
        sb.append(parent.getName());
        sb.append(" -> ");
        sb.append(child.getName());
        sb.append(TAB);
        sb.append("[Weight=");
        sb.append(parent.getChildLinkCost(child));
        sb.append("];");
        sb.append(NEWLINE);

        return sb.toString();
    }


    public static String closeGraph(){
        final StringBuffer sb = new StringBuffer();
        sb.append("}");

        return sb.toString();
    }
}
