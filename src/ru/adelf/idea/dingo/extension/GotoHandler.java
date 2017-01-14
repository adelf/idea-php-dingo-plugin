package ru.adelf.idea.dingo.extension;

import com.intellij.codeInsight.navigation.actions.GotoDeclarationHandler;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiElement;
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression;
import de.espend.idea.laravel.LaravelProjectComponent;
import fr.adrienbrault.idea.symfony2plugin.codeInsight.GotoCompletionContributor;
import fr.adrienbrault.idea.symfony2plugin.codeInsight.GotoCompletionProviderInterface;
import org.jetbrains.annotations.Nullable;
import ru.adelf.idea.dingo.utils.GotoCompletionUtil;

import java.util.ArrayList;
import java.util.Collection;

public class GotoHandler implements GotoDeclarationHandler {
    @Nullable
    @Override
    public PsiElement[] getGotoDeclarationTargets(PsiElement psiElement, int i, Editor editor) {

        if (!LaravelProjectComponent.isEnabled(psiElement)) {
            return new PsiElement[0];
        }

        Collection<PsiElement> psiTargets = new ArrayList<PsiElement>();

        PsiElement parent = psiElement.getParent();

        for(GotoCompletionContributor contributor: GotoCompletionUtil.getContributors(psiElement)) {
            GotoCompletionProviderInterface formReferenceCompletionContributor = contributor.getProvider(psiElement);
            if(formReferenceCompletionContributor != null) {
                // @TODO: replace this: just valid PHP files
                if(parent instanceof StringLiteralExpression) {
                    psiTargets.addAll(formReferenceCompletionContributor.getPsiTargets((StringLiteralExpression) parent));
                } else {
                    psiTargets.addAll(formReferenceCompletionContributor.getPsiTargets(psiElement));
                }
            }
        }

        return psiTargets.toArray(new PsiElement[psiTargets.size()]);
    }

    @Nullable
    @Override
    public String getActionText(DataContext dataContext) {
        return null;
    }
}
