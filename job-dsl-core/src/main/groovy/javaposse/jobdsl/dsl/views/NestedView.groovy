package javaposse.jobdsl.dsl.views

import javaposse.jobdsl.dsl.DslContext
import javaposse.jobdsl.dsl.JobManagement
import javaposse.jobdsl.dsl.View

import static javaposse.jobdsl.dsl.ContextHelper.executeInContext

class NestedView extends View {
    NestedView(JobManagement jobManagement) {
        super(jobManagement)
    }

    void views(@DslContext(NestedViewsContext) Closure viewsClosure) {
        NestedViewsContext context = new NestedViewsContext(jobManagement)
        executeInContext(viewsClosure, context)

        execute {
            for (View view : context.views) {
                Node viewNode = view.node
                viewNode.appendNode('name', view.name)
                viewNode.appendNode('owner', [class: 'hudson.plugins.nested_view.NestedView', reference: '../../..'])
                it / 'views' << viewNode
            }
        }
    }

    void columns(@DslContext(NestedViewColumnsContext) Closure columnsClosure) {
        NestedViewColumnsContext context = new NestedViewColumnsContext()
        executeInContext(columnsClosure, context)

        execute {
            for (Node columnNode : context.columnNodes) {
                it / 'columns' / 'columns' << columnNode
            }
        }
    }
}
