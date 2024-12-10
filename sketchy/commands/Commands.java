package sketchy.commands;

public interface Commands {
    public void undo();
    public void redo();
    public void execute();
}
