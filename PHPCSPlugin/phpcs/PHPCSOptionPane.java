package phpcs;

import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JCheckBox;

import org.gjt.sp.jedit.AbstractOptionPane;
import org.gjt.sp.jedit.jEdit;

public class PHPCSOptionPane extends AbstractOptionPane {
    private JTextField phpcsPath;
    private JTextField phpcsStandard;
    private JCheckBox  phpcsRunOnSave;

    public PHPCSOptionPane() {
        super("phpcs");
    }

    public void _init() {
        this.phpcsPath      = new JTextField(jEdit.getProperty("options.phpcs.path"), 20);
        this.phpcsStandard  = new JTextField(jEdit.getProperty("options.phpcs.standard"), 10);
        this.phpcsRunOnSave = new JCheckBox("Run on Save",jEdit.getBooleanProperty("options.phpcs.runonsave"));

        JPanel panel = new JPanel(new GridLayout(3, 2, 1, 1));
        panel.add(new JLabel("Path to PHP_CodeSniffer"));
        panel.add(this.phpcsPath);
        panel.add(new JLabel("Coding Standard to Use"));
        panel.add(this.phpcsStandard);
        panel.add(new JLabel(""));
        panel.add(this.phpcsRunOnSave);
        addComponent(panel);
    }

    public void _save() {
        jEdit.setProperty("options.phpcs.path", this.phpcsPath.getText());
        jEdit.setProperty("options.phpcs.standard", this.phpcsStandard.getText());
        jEdit.setBooleanProperty("options.phpcs.runonsave",this.phpcsRunOnSave.isSelected());
    }
}
