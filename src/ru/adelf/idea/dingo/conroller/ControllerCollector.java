package ru.adelf.idea.dingo.conroller;

import com.intellij.openapi.project.Project;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.psi.elements.Method;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Copy of {@link de.espend.idea.laravel.controller.ControllerCollector} . @todo use original
 */
public class ControllerCollector {

    private static final Set<String> commonControllerTraits = getCommonControllerTraits();

    public static void visitControllerActions(@NotNull final Project project, @NotNull ControllerActionVisitor visitor) {

        Collection<PhpClass> allSubclasses = new HashSet<PhpClass>() {{
            addAll(PhpIndex.getInstance(project).getAllSubclasses("\\Illuminate\\Routing\\Controller"));
            addAll(PhpIndex.getInstance(project).getAllSubclasses("\\App\\Http\\Controllers\\Controller"));
        }};

        for(PhpClass phpClass: allSubclasses) {
            if(!phpClass.isAbstract()) {
                for(Method method: phpClass.getMethods()) {
                    String className = phpClass.getPresentableFQN();
                    String methodName = method.getName();
                    if(!method.isStatic() && method.getAccess().isPublic() && !methodName.startsWith("__")) {
                        PhpClass phpTrait = method.getContainingClass();
                        if(phpTrait == null || !commonControllerTraits.contains(phpTrait.getName())) {
                            visitor.visit(phpClass, method, className + "@" + methodName);
                        }
                    }
                }
            }
        }
    }

    public static void visitController(@NotNull final Project project, @NotNull ControllerVisitor visitor) {

        Collection<PhpClass> allSubclasses = new HashSet<PhpClass>() {{
            addAll(PhpIndex.getInstance(project).getAllSubclasses("\\Illuminate\\Routing\\Controller"));
            addAll(PhpIndex.getInstance(project).getAllSubclasses("\\App\\Http\\Controllers\\Controller"));
        }};

        for(PhpClass phpClass: allSubclasses) {

            if(phpClass.isAbstract()) {
                continue;
            }

            visitor.visit(phpClass, phpClass.getPresentableFQN());
        }
    }

    @NotNull
    private static Set<String> getCommonControllerTraits() {
        Set<String> traits = new HashSet<String>();

        traits.add("ValidatesRequests");
        traits.add("DispatchesCommands");
        traits.add("DispatchesJobs"); // For laravel >=5.3
        traits.add("AuthorizesRequests");
        traits.add("Controller");

        return traits;
    }

    public interface ControllerVisitor {
        void visit(@NotNull PhpClass phpClass, @NotNull String name);
    }

    public interface ControllerActionVisitor {
        void visit(@NotNull PhpClass phpClass, @NotNull Method method, @NotNull String name);
    }
}