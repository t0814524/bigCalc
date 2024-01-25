import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class BigCalcVisitorImpl extends BigCalcBaseVisitor<BigDecimal> {

  public Map<String, BigDecimal> symbolTable = new HashMap();

  @Override
  public BigDecimal visitProgram(BigCalcParser.ProgramContext ctx) {
    BigDecimal res = null;
    for (BigCalcParser.StatementContext statementContext : ctx.statement()) {
      res = visitStatement(statementContext);
    }
    return res;
  }

  @Override
  public BigDecimal visitStatement(BigCalcParser.StatementContext ctx) {
    if (ctx.expressionStatement() != null) {
      return visitExpressionStatement(ctx.expressionStatement());
    } else {
      return visitAssignmentStatement(ctx.assignmentStatement());
    }
  }

  @Override
  public BigDecimal visitExpressionStatement(
    BigCalcParser.ExpressionStatementContext ctx
  ) {
    return visit(ctx.expression());
  }

  @Override
  public BigDecimal visitParen(BigCalcParser.ParenContext ctx) {
    return visit(ctx.expression());
  }

  @Override
  public BigDecimal visitAssignmentStatement(
    BigCalcParser.AssignmentStatementContext ctx
  ) {
    // System.out.println("assignmentstatement vistit");
    // BigCalcParser.AssignmentContext asdf = ctx.assignment(); // nullptr error
    // System.out.println("assignmentstatement vistit2"); // not printed if line above is active

    try {
      return visit(ctx.assignment());
    } catch (Exception e) {
      // System.out.println(e.getMessage());
      // e.printStackTrace();
    }
    return new BigDecimal(0);
  }

  @Override
  public BigDecimal visitAssignment(BigCalcParser.AssignmentContext ctx) {
    BigDecimal res = visit(ctx.expression());
    if (ctx.Variable() != null) {
      String variableName = ctx.Variable().getText();
      this.symbolTable.put(variableName, res);
    }
    return res;
  }

  @Override
  public BigDecimal visitMulDiv(BigCalcParser.MulDivContext ctx) {
    final BigDecimal left = visit(ctx.expression(0));
    final BigDecimal right = visit(ctx.expression(1));
    if (ctx.op.getText().equals("*")) {
      return left.multiply(right);
    } else {
      return left.divide(right, 10, RoundingMode.HALF_UP);
    }
  }

  @Override
  public BigDecimal visitAddSub(BigCalcParser.AddSubContext ctx) {
    final BigDecimal left = visit(ctx.expression(0));
    final BigDecimal right = visit(ctx.expression(1));
    if (ctx.op.getText().equals("+")) {
      return left.add(right);
    } else {
      return left.subtract(right);
    }
  }

  @Override
  public BigDecimal visitNum(BigCalcParser.NumContext ctx) {
    return new BigDecimal(ctx.Number().getText());
  }

  @Override
  public BigDecimal visitVar(BigCalcParser.VarContext ctx) {
    String variableName = ctx.getText();
    // System.out.println("visti var");
    // System.out.println(variableName);
    BigDecimal val =
      this.symbolTable.getOrDefault(variableName, new BigDecimal(0));
    // System.out.println(val);
    return val;
  }
}
