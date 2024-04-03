package com.company.nsmtemplate.view.step

import com.company.nsmtemplate.entity.Step
import com.company.nsmtemplate.view.main.MainView
import com.vaadin.flow.router.Route
import io.jmix.flowui.view.*

@Route(value = "steps/:id", layout = MainView::class)
@ViewController("Step.detail")
@ViewDescriptor("step-detail-view.xml")
@EditedEntityContainer("stepDc")
class StepDetailView : StandardDetailView<Step>() {
}