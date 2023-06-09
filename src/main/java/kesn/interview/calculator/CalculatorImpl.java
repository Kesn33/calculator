package kesn.interview.calculator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.Stack;

public class CalculatorImpl implements Calculator{

    /**
     * 现时的计算结果
     */
    private BigDecimal result;

    /**
     * 存储操作记录（除redos，undo）
     */
    private Stack<String> operatorCh;

    private Stack<BigDecimal> resultHistory;

    private final int SCALE = 2;

    public CalculatorImpl() {
        result = BigDecimal.ZERO;
        operatorCh = new Stack<String>();
        resultHistory = new Stack<BigDecimal>();
        resultHistory.push(BigDecimal.ZERO);
    }

    public synchronized Calculator add(BigDecimal value) {
        result = result.add(value);
        operatorCh.push("+");
        operatorCh.push(value.toString());
        resultHistory.push(result);
        printOperator();
        return this;
    }

    public synchronized Calculator sub(BigDecimal value) {
        result = result.subtract(value);
        operatorCh.push("-");
        operatorCh.push(value.toString());
        resultHistory.push(result);
        printOperator();
        return this;
    }

    public synchronized Calculator mul(BigDecimal value) {
        result = result.multiply(value);
        operatorCh.push("*");
        operatorCh.push(value.toString());
        resultHistory.push(result);
        printOperator();
        return this;
    }

    public synchronized Calculator div(BigDecimal value) {
        if (Objects.equals(BigDecimal.ZERO,value)){
            throw new RuntimeException("除数不能为0");
        }
        result = result.divide(value,SCALE, RoundingMode.HALF_UP);
        operatorCh.push("/");
        operatorCh.push(value.toString());
        resultHistory.push(result);
        printOperator();
        return this;
    }

    public synchronized Calculator redo() {
        System.out.println("redo");
        result = BigDecimal.ZERO;
        operatorCh = new Stack<String>();
        resultHistory = new Stack<BigDecimal>();
        resultHistory.push(BigDecimal.ZERO);
        return this;
    }

    // 撤回上一步操作
    public synchronized Calculator undo() {
        if (operatorCh.isEmpty() || resultHistory.isEmpty()){
            throw new RuntimeException("can not undo");
        }
        //回滚到上一步的情况
        operatorCh.pop();
        operatorCh.pop();
        resultHistory.pop();
        BigDecimal lastResult = resultHistory.peek();
        result = lastResult;
        System.out.println("undo to:");
        printOperator();
        return this;
    }

    private void printOperator(){
        if (operatorCh.isEmpty()){
            System.out.println("operator record is empty");
            return;
        }
        String curNum = operatorCh.pop();
        String curOperator = operatorCh.pop();
        BigDecimal curResult = resultHistory.pop();
        BigDecimal lastResult = resultHistory.pop();
        System.out.println(String.format("%s %s %s = %s",lastResult.toString(),curOperator,curNum,curResult.toString()));
        operatorCh.push(curOperator);
        operatorCh.push(curNum);
        resultHistory.push(lastResult);
        resultHistory.push(curResult);
    }

    @Override
    public BigDecimal getResult() {
        System.out.println(String.format("result is %f",result));
        return result;
    }
}
