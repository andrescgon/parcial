import java.util.HashMap;
import java.util.Map;

public class ExprEvalVisitor extends ExprBaseVisitor<Integer> {
    // "memoria" para las variables
    Map<String, Integer> memory = new HashMap<>();

    // Asignación de variable
    @Override
    public Integer visitAssign(ExprParser.AssignContext ctx) {
        String id = ctx.ID().getText(); // identificador a la izquierda del '='
        int value = visit(ctx.expr()); // calcular el valor de la expresión a la derecha
        memory.put(id, value); // almacenar en la memoria
        return value;
    }

    // Imprimir expresión
    @Override
    public Integer visitPrintExpr(ExprParser.PrintExprContext ctx) {
        Integer value = visit(ctx.expr()); // evaluar la expresión
        System.out.println(value); // imprimir el resultado
        return 0; // valor dummy
    }

    // Número entero
    @Override
    public Integer visitInt(ExprParser.IntContext ctx) {
        return Integer.valueOf(ctx.INT().getText());
    }

    // Identificador
    @Override
    public Integer visitId(ExprParser.IdContext ctx) {
        String id = ctx.ID().getText();
        if (memory.containsKey(id)) return memory.get(id);
        return 0;
    }

    // Multiplicación y división
    @Override
    public Integer visitMulDiv(ExprParser.MulDivContext ctx) {
        int left = visit(ctx.expr(0)); // valor de la subexpresión izquierda
        int right = visit(ctx.expr(1)); // valor de la subexpresión derecha
        if (ctx.op.getType() == ExprParser.MUL) return left * right;
        if (right == 0) throw new ArithmeticException("Division by zero");
        return left / right; // debe ser DIV
    }

    // Suma y resta
    @Override
    public Integer visitAddSub(ExprParser.AddSubContext ctx) {
        int left = visit(ctx.expr(0)); // valor de la subexpresión izquierda
        int right = visit(ctx.expr(1)); // valor de la subexpresión derecha
        if (ctx.op.getType() == ExprParser.ADD) return left + right;
        return left - right; // debe ser SUB
    }

    // Expresión entre paréntesis
    @Override
    public Integer visitParens(ExprParser.ParensContext ctx) {
        return visit(ctx.expr()); // devolver el valor de la expresión dentro de paréntesis
    }
}
