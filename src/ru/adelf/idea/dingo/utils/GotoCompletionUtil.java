package ru.adelf.idea.dingo.utils;

import com.intellij.psi.PsiElement;
import fr.adrienbrault.idea.symfony2plugin.codeInsight.GotoCompletionContributor;
import fr.adrienbrault.idea.symfony2plugin.codeInsight.GotoCompletionRegistrar;
import fr.adrienbrault.idea.symfony2plugin.codeInsight.GotoCompletionRegistrarParameter;
import ru.adelf.idea.dingo.conroller.ControllerReferences;

import java.util.ArrayList;
import java.util.Collection;

public class GotoCompletionUtil {

    private static GotoCompletionRegistrar[] CONTRIBUTORS = new GotoCompletionRegistrar[] {
            new ControllerReferences()
    };

    public static Collection<GotoCompletionContributor> getContributors(final PsiElement psiElement) {

        final Collection<GotoCompletionContributor> contributors = new ArrayList<>();

        GotoCompletionRegistrarParameter registrar = (pattern, contributor) -> {
            if(pattern.accepts(psiElement)) {
                contributors.add(contributor);
            }
        };

        for(GotoCompletionRegistrar register: CONTRIBUTORS) {
            register.register(registrar);
        }

        return contributors;
    }

}
