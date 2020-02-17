package kquery

class QueryPlanner {

    //TODO optimization rules

    /**
     * Create a physical plan from a logical plan.
     */
    fun createPhysicalPlan(plan: LogicalPlan) : PhysicalPlan {
        return when (plan) {
            is Scan -> DataSourceExec(plan.dataSource, plan.projection)
            is Selection -> SelectionExec(createPhysicalPlan(plan.input), createPhysicalExpr(plan.expr))
            is Projection -> ProjectionExec(createPhysicalPlan(plan.input), plan.schema(), plan.expr.map { createPhysicalExpr(it) })
            else -> throw IllegalStateException(plan.javaClass.toString())
        }
    }

    /**
     * Create a physical expression from a logical expression.
     */
    fun createPhysicalExpr(expr: Expr): PhysicalExpr {
        return when (expr) {
            is Column -> ColumnExpr(expr)
            else -> throw IllegalStateException(expr.javaClass.toString())
        }

    }
}