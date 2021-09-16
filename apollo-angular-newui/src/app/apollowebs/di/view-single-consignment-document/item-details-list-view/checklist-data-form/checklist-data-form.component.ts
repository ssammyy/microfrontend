import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MatDialog} from "@angular/material/dialog";
import {DestinationInspectionService} from "../../../../../core/store/data/di/destination-inspection.service";
import swal from "sweetalert2";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
    selector: 'app-checklist-data-form',
    templateUrl: './checklist-data-form.component.html',
    styleUrls: ['./checklist-data-form.component.css']
})
export class ChecklistDataFormComponent implements OnInit {
    generalCheckList: FormGroup
    categories: any[]
    checkListTypes: any[]
    laboratories: []
    message: any
    itemList: any[]
    vehicleDetails: any
    vehicleItems: any[]
    otherDetails: any
    otherItems: any[]
    engineeringDetails: any
    engineringItems: any[]
    agrochemDetails: any
    agrochemItems: any[]
    selectedChecklist: any
    configs: any
    consignmentId: any
    consignment: any
    errors: any

    constructor(private fb: FormBuilder, private dialog: MatDialog, private router: Router, private activatedRoute: ActivatedRoute,
                private diService: DestinationInspectionService) {
    }

    ngOnInit(): void {
        this.activatedRoute.paramMap.subscribe(
            res => {
                this.consignmentId = res.get("id")
                this.loadConsignmentDetails()
                this.loadChecklistConfigurations()
            }
        )
        this.generalCheckList = this.fb.group({
            inspection: ['', Validators.required],
            clearingAgent: ['', [Validators.required, Validators.maxLength(256)]],
            overallRemarks: ['', [Validators.required, Validators.maxLength(256)]],
            customsEntryNumber: ['', [Validators.required, Validators.maxLength(256)]],
        })
    }

    loadConsignmentDetails() {
        this.diService.getConsignmentDetails(this.consignmentId)
            .subscribe(
                response => {
                    if (response.responseCode === "00") {
                        this.consignment = response.data
                        this.itemList = this.consignment.items_cd
                    } else {
                        swal.fire({
                            title: response.message,
                            buttonsStyling: false,
                            customClass: {
                                confirmButton: 'btn btn-success form-wizard-next-btn ',
                            },
                            icon: 'error'
                        }).then(() => {
                            this.router.navigate(["/di", this.consignmentId])
                        });
                        console.log(response)
                    }
                }
            )
    }

    loadChecklistConfigurations() {
        this.diService.loadChecklistConfigs()
            .subscribe(
                res => {
                    if (res.responseCode === "00") {
                        this.configs = res.data
                        this.categories = this.configs.categories
                        this.checkListTypes = this.configs.checkListTypes
                        this.laboratories = this.configs.laboratories
                    }
                }
            )
    }

    setAgrochemChecklist(data: any) {
        this.agrochemDetails = data
    }

    setEngineringChecklist(data: any) {
        this.engineeringDetails = data
    }

    setVehicleChecklist(data: any) {
        this.vehicleDetails = data
    }

    setOtherChecklist(data: any) {
        this.otherDetails = data
    }

    getItems(itemList: any[]): any[] {
        let items = []
        if (itemList) {
            itemList.forEach(itm => {
                items.push({
                    ...{
                        "itemId": itm.id,
                        "category": itm.category,
                        "compliant": itm.compliant,
                        "sampled": itm.sampled,
                        "serialNumber": itm.serialNumber
                    }, ...itm.checklist
                })
            })
        }
        return items
    }

    validateItems() {
        let selectedItems = []
        selectedItems.push(this.engineringItems)
        selectedItems.push(this.agrochemItems)
        selectedItems.push(this.otherItems)
        selectedItems.push(this.vehicleItems)
        if (selectedItems.length > this.itemList.length) {
            this.errors["selected"] = "Some items were selected more than once"
        }
        if (this.generalCheckList.value.inspection == "FULL" && selectedItems.length < this.itemList.length) {
            this.errors["inspection"] = "Full inspection requires all items to be selected"
        }
    }


    invalidData(): Boolean {
        this.errors = {}
        this.validateItems()
        if (this.engineringItems && this.engineringItems.length > 0 && !this.engineeringDetails) {
            this.errors["engineering"] = "Fill engineering details"
        }
        if (this.agrochemItems && this.agrochemItems.length > 0 && !this.agrochemDetails) {
            this.errors["agrochem"] = "Fill agrochem details"
        }
        if (this.vehicleItems && this.vehicleItems.length > 0 && !this.vehicleDetails) {
            this.errors["vehicle"] = "Fill vehicle details"
        }
        if (this.otherItems && this.otherItems.length > 0 && !this.otherDetails) {
            this.errors["other"] = "Fill other details"
        }
        return this.errors.length > 0
    }

    checklistChanges(event: any) {
        console.log(event)
        let selections = this.checkListTypes.filter(d => d.id == event.target.value)
        if (selections.length > 0) {
            this.selectedChecklist = selections[0]
        }
    }

    saveRecord() {
        this.message = null
        if (this.invalidData()) {
            this.message = "Please correct form errors"
            return
        }
        let data = this.generalCheckList.value
        if (this.engineeringDetails) {
            this.engineeringDetails["items"] = this.getItems(this.engineringItems)
            data["engineering"] = this.engineeringDetails
        }
        if (this.vehicleDetails) {
            this.vehicleDetails["items"] = this.getItems(this.vehicleItems)
            data["vehicle"] = this.vehicleDetails
        }
        if (this.agrochemDetails) {
            this.agrochemDetails["items"] = this.getItems(this.agrochemItems)
            data["agrochem"] = this.agrochemDetails
        }
        if (this.otherDetails) {
            this.otherDetails["items"] = this.getItems(this.otherItems)
            data["others"] = this.otherDetails
        }
        this.diService.saveChecklist(this.consignmentId, data)
            .subscribe(
                res => {
                    if (res.responseCode === "00") {
                        this.router.navigate(["/di", this.consignmentId])
                    } else {
                        this.message = res.message
                    }
                }
            )
    }

}
