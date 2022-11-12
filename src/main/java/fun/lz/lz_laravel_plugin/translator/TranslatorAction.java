package fun.lz.lz_laravel_plugin.translator;

import cn.hutool.core.util.StrUtil;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import org.jetbrains.annotations.NotNull;

public class TranslatorAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        if (StrUtil.isBlank(TranslatorUtils.appID) || StrUtil.isBlank(TranslatorUtils.securityKey)) {
            Notifications.Bus.notify(
                    new Notification(
                            "Translator",
                            "小天才翻译机",
                            "请先设置appID，securityKey",
                            NotificationType.ERROR
                    ),
                    e.getProject()
            );
            return;
        }

        var editor = e.getData(CommonDataKeys.EDITOR);
        if (editor == null) {
            return;
        }

        var text = editor.getSelectionModel().getSelectedText();
        if (StrUtil.isBlank(text)) {
            return;
        }

        if (e.getProject() == null) {
            return;
        }
        var transCache = TranslatorCache.getInstance(e.getProject()).transCache;
        String transResult;
        if (transCache.containsKey(text)) {
            transResult = transCache.get(text);
        } else {
            transResult = TranslatorUtils.getTransResult(text, "auto", "zh");
            transCache.put(text, transResult);
        }

        TranslatorToolsWindow.addNote(text, transResult);

        Notifications.Bus.notify(
                new Notification(
                        "Translator",
                        "小天才翻译机",
                        transResult,
                        NotificationType.INFORMATION
                ),
                e.getProject()
        );
    }
}
