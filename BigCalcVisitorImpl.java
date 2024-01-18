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
    System.out.println(res);
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
    System.out.println("paren");
    System.out.println(ctx.getText());
    return visit(ctx.expression());
  }

  @Override
  public BigDecimal visitAssignmentStatement(
    BigCalcParser.AssignmentStatementContext ctx
  ) {
    System.out.println("assignmentstatement vistit");
    System.out.println(ctx.getText());
    return visit(ctx.assignment());
  }

  @Override
  public BigDecimal visitAssignment(BigCalcParser.AssignmentContext ctx) {
    System.out.println("ass vistit");
    BigDecimal res = visit(ctx.expression());
    String variableName = ctx.Variable().getText();
    this.symbolTable.put(variableName, res);
    return res;
  }

  @Override
  public BigDecimal visitMulDiv(BigCalcParser.MulDivContext ctx) {
    final BigDecimal left = visit(ctx.expression(0));
    final BigDecimal right = visit(ctx.expression(1));
    if (ctx.op.getText().equals("*")) {
      System.out.println("visit div");
      return left.multiply(right);
    } else {
      System.out.println("visit div");
      System.out.println(left);
      System.out.println(right);
      return left.divide(right, 10, RoundingMode.HALF_UP);
    }
  }

  @Override
  public BigDecimal visitAddSub(BigCalcParser.AddSubContext ctx) {
    final BigDecimal left = visit(ctx.expression(0));
    final BigDecimal right = visit(ctx.expression(1));
    if (ctx.op.getText().equals("+")) {
      System.out.println("visit add");
      return left.add(right);
    } else {
      System.out.println("visit sub");
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
    System.out.println("visti var");
    System.out.println(variableName);
    BigDecimal val =
      this.symbolTable.getOrDefault(variableName, new BigDecimal(0));
    System.out.println(val);
    return val;
  }
}
