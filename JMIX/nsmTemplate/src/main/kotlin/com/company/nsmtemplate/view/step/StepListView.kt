package com.company.nsmtemplate.view.step

import com.company.nsmtemplate.entity.Step
import com.company.nsmtemplate.view.main.MainView
import com.vaadin.flow.router.Route
import io.jmix.flowui.view.*

@Route(value = "steps", layout = MainView::class)
@ViewController("Step.list")
@ViewDescriptor("step-list-view.xml")
@LookupComponent("stepsDataGrid")
@DialogMode(width = "64em")
class StepListView : StandardListView<Step>() {
}