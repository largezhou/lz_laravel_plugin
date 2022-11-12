package fun.lz.lz_laravel_plugin.translator;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.content.ContentFactory;
import com.intellij.ui.table.JBTable;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class TranslatorToolsWindow implements ToolWindowFactory {
    private static JTable table;

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        // ContentFactory 在 IntelliJ 平台 SDK 中负责 UI 界面的管理
        var contentFactory = ContentFactory.getInstance();
        // 创建我们的工具栏界面，TranslatorNote 是基于 Swing 实现的一个窗口视图
        var note = new TranslatorNote();
        table = note.getTable();
        // 在界面工厂中创建翻译插件的界面
        var content = contentFactory.createContent(note.getNotePanel(), "", false);
        // 将被界面工厂代理后创建的content，添加到工具栏窗口管理器中
        toolWindow.getContentManager().addContent(content);
    }

    public static void addNote(String from, String to) {
        if (table == null) {
            return;
        }

        var tableModel = (DefaultTableModel) table.getModel();
        tableModel.addRow(new Object[]{from, to});
    }

    @Getter
    static class TranslatorNote {
        private final JScrollPane notePanel;
        private final JTable table;

        public TranslatorNote() {
            String[] header = {"原文", "翻译"};
            var tableModel = new DefaultTableModel(null, header);
            this.table = new JBTable();
            this.table.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
            this.table.setModel(tableModel);

            this.notePanel = new JBScrollPane(table);
            this.notePanel.setSize(200, 200);
        }
    }
}
