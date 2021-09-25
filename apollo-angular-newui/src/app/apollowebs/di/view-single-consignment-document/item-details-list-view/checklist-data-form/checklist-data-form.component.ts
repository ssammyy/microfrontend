import {Component, OnInit} from '@angular/core';
import {Form, FormBuilder, FormGroup, Validators} from "@angular/forms";
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
    stations: any[]
    checkListTypes: any[]
    laboratories: []
    message: any
    itemList: any[]
    vehicleDetails: any
    vehicleValid: Boolean = true
    otherDetails: any
    otherValid: Boolean = true
    engineeringDetails: any
    engineringValid: Boolean = true
    agrochemDetails: any
    agroChemValid: Boolean = true
    configs: any
    consignmentId: any
    consignment: any
    errors: any[]
    invalidSelection: Boolean = false;

    constructor(private fb: FormBuilder, private dialog: MatDialog, private router: Router, private activatedRoute: ActivatedRoute,
                private diService: DestinationInspectionService) {
    }

    ngOnInit(): void {
        this.errors = []
        this.activatedRoute.paramMap.subscribe(
            res => {
                this.consignmentId = res.get("id")
                this.loadConsignmentDetails()
                this.loadChecklistConfigurations()
                this.loadMinistryStations()
            }
        )
        this.generalCheckList = this.fb.group({
            inspection: ['', Validators.required],
            clearingAgent: ['', [Validators.required, Validators.maxLength(256)]],
            overallRemarks: ['', [Validators.required, Validators.maxLength(256)]],
            customsEntryNumber: ['', [Validators.required, Validators.maxLength(256)]],
        })
        // Validate on change
        this.generalCheckList.statusChanges.subscribe(
            res => {
                console.log(res)
                this.invalidSelection = this.invalidData()
            }
        )
    }

    goBack() {
        this.router.navigate(["/di", this.consignmentId])
    }

    validationUpdate(value: Boolean, checklist: any) {
        switch (checklist) {
            case 'engineering':
                this.engineringValid = value
                break
            case "agrochem":
                this.agroChemValid = value
                break
            case 'other':
                this.otherValid = value
                break
            case 'vehicle':
                this.vehicleValid = value
                break
            default:
                console.log("invalid checklist: " + checklist)
        }
        this.invalidSelection = this.invalidData()
    }

    loadMinistryStations() {
        this.diService.loadMinistryStations()
            .subscribe(
                res => {
                    if (res.responseCode === "00") {
                        this.stations = res.data
                    } else {
                        console.log("Failed to load stations")
                    }
                }
            )

    }

    loadConsignmentDetails() {
        this.diService.getConsignmentDetails(this.consignmentId)
            .subscribe(
                response => {
                    if (response.responseCode === "00") {
                        this.consignment = response.data
                        this.itemList = this.consignment.items_cd
                    } else {
                        this.diService.showError(response.message, () => {
                            this.router.navigate(["/di", this.consignmentId])
                        })
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
        this.invalidSelection = this.invalidData()
    }

    setEngineringChecklist(data: any) {
        this.engineeringDetails = data
        this.invalidSelection = this.invalidData()
    }

    setVehicleChecklist(data: any) {
        this.vehicleDetails = data
        this.invalidSelection = this.invalidData()
    }

    setOtherChecklist(data: any) {
        this.otherDetails = data
        this.invalidSelection = this.invalidData()
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
        let selectedItems = 0
        for (let itm of this.itemList) {
            if (itm.selected) {
                selectedItems = selectedItems + 1;
            }
        }
        if (this.generalCheckList.value.inspection == "FULL" && selectedItems < this.itemList.length) {
            this.errors.push({
                type: "inspection",
                description: "Full inspection requires all items to be selected"
            })
        }
    }


    invalidData(): Boolean {
        this.errors = []
        this.validateItems()
        if (!this.engineringValid) {
            this.errors.push({
                type: "engineering",
                description: "Fill engineering details"
            })
        }
        if (!this.agroChemValid) {
            this.errors.push({
                type: "agrochem",
                description: "Fill agrochem details"
            })
        }
        if (!this.vehicleValid) {
            this.errors.push({
                type: "vehicle",
                description: "Fill vehicle details"
            })
        }
        if (!this.otherValid) {
            this.errors.push({
                type: "other",
                description: "Fill other details"
            })
        }
        if(this.generalCheckList.valid) {
            if (!this.vehicleDetails && !this.otherDetails && !this.agrochemDetails && !this.engineeringDetails) {
                this.errors.push({
                    type: "checklist",
                    description: "Please fill at least one checklist"
                })
            }
        }
        return this.errors.length > 0
    }

    saveRecord() {
        this.message = null
        if (this.invalidData()) {
            this.message = "Please correct form errors"
            return
        }
        let data = this.generalCheckList.value
        if (this.engineeringDetails) {
            this.engineeringDetails["items"] = this.getItems(this.engineeringDetails.items)
            data["engineering"] = this.engineeringDetails
        }
        if (this.vehicleDetails) {
            this.vehicleDetails["items"] = this.getItems(this.vehicleDetails.items)
            data["vehicle"] = this.vehicleDetails
        }
        if (this.agrochemDetails) {
            this.agrochemDetails["items"] = this.getItems(this.agrochemDetails.items)
            data["agrochem"] = this.agrochemDetails
        }
        if (this.otherDetails) {
            this.otherDetails["items"] = this.getItems(this.otherDetails.items)
            data["others"] = this.otherDetails
        }
        this.diService.saveChecklist(this.consignmentId, data)
            .subscribe(
                res => {
                    if (res.responseCode === "00") {
                        this.router.navigate(["/di/checklist/details", this.consignmentId])
                    } else {
                        this.message = res.message
                    }
                }
            )
    }

}
