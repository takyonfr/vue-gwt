package com.axellience.vuegwt.core.template.parser.result;

import com.axellience.vuegwt.core.template.parser.variable.VariableInfo;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;
import jsinterop.base.Any;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static com.axellience.vuegwt.core.generation.GenerationUtil.stringTypeToTypeName;

/**
 * A Java expression from the template.
 * For each Java expression in the template, a {@link TemplateExpression} is created.
 * The Java expression from the template is then replaced by the result of {@link
 * TemplateExpression#toTemplateString()}.
 * This expression will be either by a computed property or a Method in the Vue.js Component.
 * @author Adrien Baron
 */
public class TemplateExpression
{
    private final String id;
    private final String body;
    private final TypeName type;
    private final List<VariableInfo> parameters = new LinkedList<>();

    public TemplateExpression(String id, String body, String type,
        Collection<VariableInfo> parameters)
    {
        this.id = id;
        this.type = stringTypeToTypeName(type);
        this.body = body;
        this.parameters.addAll(parameters);
    }

    /**
     * Return the ID of this expression. It's the name used in the template for it.
     * @return The id as a String
     */
    public String getId()
    {
        return id;
    }

    /**
     * Java type of the expression.
     * @return The fully qualified name of the returned Java type
     */
    public TypeName getType()
    {
        return type;
    }

    /**
     * List of parameters this expression depends upon
     * @return The list of parameters for this expression
     */
    public List<VariableInfo> getParameters()
    {
        return parameters;
    }

    /**
     * The body of the expression. This is what was in the template and that must be returned by
     * this expression in Java.
     * @return The body of the expression
     */
    public String getBody()
    {
        return body;
    }

    /**
     * Return this expression as a string that can be placed in the template as a replacement
     * of the Java expression.
     * @return An expression that can be interpreted by Vue.js
     */
    public String toTemplateString()
    {
        String[] parametersName =
            this.parameters.stream().map(VariableInfo::getName).toArray(String[]::new);

        return this.getId() + "(" + String.join(", ", parametersName) + ")";
    }

    public boolean isReturnVoid()
    {
        return type == TypeName.VOID;
    }

    public boolean isReturnAny()
    {
        return Any.class.getCanonicalName().equals(type.toString());
    }

    public boolean isReturnString()
    {
        if (!(type instanceof ClassName))
            return false;

        ClassName className = (ClassName) type;
        return className.reflectionName().equals("String") || className
            .reflectionName()
            .equals(String.class.getCanonicalName());
    }
}
