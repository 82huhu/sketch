package sketchy.commands;

/**
 * Commands is an interface which encapsulates all the commands
 */
public interface Commands {
    public void undo();
    public void redo();
    public void execute();
}
