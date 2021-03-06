package ru.adelf.idea.dingo.conroller.namespace;

import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.Nullable;

public class StandardControllerNamespaceCutter implements ControllerNamespaceCutter {

    private String prefix;

    public StandardControllerNamespaceCutter(@Nullable String prefix) {

        this.prefix = prefix;
    }

    @Override
    public void cut(String className, ControllerClassNameProcessor processor) {

        if(StringUtils.isNotBlank(prefix) && className.startsWith(prefix)) {
            processor.process(className.substring(prefix.length() + 1), true);
            return;
        }

        processor.process(className, false);
    }
}