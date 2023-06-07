import {Component, Inject, Input, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {DestinationInspectionService} from "../../../../core/store/data/di/destination-inspection.service";
import {SelectionModel} from "@angular/cdk/collections";
import {MatTableDataSource} from "@angular/material/table";

@Component({
    selector: 'app-send-demand-note-tokws',
    templateUrl: './send-demand-note-tokws.component.html',
    styleUrls: ['./send-demand-note-tokws.component.css']
})
export class SendDemandNoteTokwsComponent implements OnInit {
    pricingDisplayedColumns: string[] = ['product', 'feeName', 'cfvalue', 'rate', 'amountPayable', 'minimumAmount', 'maximumAmount', 'adjustedAmount', 'actions'];
    displayedColumns: string[] = ['select', 'hsCode', 'description', 'quantity', 'unit_price', 'price', 'currency', 'pricing'];
    @Input() items: any[]
    paymentFees: any[]
    presentmentData: any
    message: any
    saveDisabled: Boolean = false
    public form: FormGroup;
    public groupForm: FormGroup
    initialSelection: any[]
    presentmentRequest: any
    selectionDataSource: MatTableDataSource<any>
    itemPricingDataSource: MatTableDataSource<any>
    selection: SelectionModel<any>
    targetPricingSelection: any[]
    invalidFeeSelection: Boolean;
    groupFeeApplicable: Boolean = false;


    constructor(public dialogRef: MatDialogRef<any>, private fb: FormBuilder, @Inject(MAT_DIALOG_DATA) public data: any,
                private diService: DestinationInspectionService) {
    }

    ngOnInit(): void {
        this.form = this.fb.group({
            remarks: ['', [Validators.required, Validators.minLength(5)]],
            amount: ['']
        })

        this.groupForm = this.fb.group({
            feeId: ['', [Validators.required]],
            group: ['GROUP', [Validators.required]]
        })
        this.items = this.data.items
        // Selection
        this.selectionDataSource = new MatTableDataSource<any>(this.items)
        this.targetPricingSelection = []
        // Item price
        this.itemPricingDataSource = new MatTableDataSource<any>(this.items)
        this.paymentFees = this.data.paymentFees
        this.initialSelection = this.items
        this.invalidFeeSelection = true
        this.selection = new SelectionModel<any>(true, this.initialSelection);
        this.groupFeeApplicable = this.checkGroupPricingApplicable()
        this.selection.changed
            .subscribe(
                res => {
                    console.log("Selection changes: " + res)
                    this.invalidFeeSelection = this.checkPricingSelected()
                }
            )
    }

    getItems() {
        let itemList = []
        for (let i of this.targetPricingSelection) {
            if (i.feeId) {
                itemList.push({
                    "itemId": i.id,
                    "items": i.items,
                    "group": i.group,
                    "feeId": i.feeId.id,
                })
            }
        }
        return itemList
    }

    // Remove item from list of selected demand notes
    // Recalculate the total amount
    removeDemandNoteItem(rowData, index: number) {
        // Clear fee id
        this.targetPricingSelection.splice(index, 1)
        // Reload
        this.saveRecord(true)

    }

    selectionChanged(event, row) {
        event ? this.selection.toggle(row) : null
        this.groupFeeApplicable = this.checkGroupPricingApplicable()
        this.invalidFeeSelection = this.checkPricingSelected()
        if (!this.invalidFeeSelection) {
            this.saveRecord(true)
        }
    }

    // Returns true if any item did not have a selected fee
    // Returns false if all items had fee selected
    checkPricingSelected(): Boolean {
        for (let i of this.selection.selected) {
            if (!i.feeId || !i.feeId.id || i.feeId == "None") {
                return true
            }
        }
        return false
    }

    // Checks if group pricing can be applied
    // This is applicable when all items in selection have same feeId or have no fee applied
    checkGroupPricingApplicable(): Boolean {
        return this.selection.selected.length > 1
    }

    findFeeById(feeData: string) {
        let feeId = parseInt(feeData)
        console.log("Group pricing: " + feeId)
        let fee = null
        for (let f of this.paymentFees) {
            if (f.id === feeId) {
                fee = f;
            }
        }
        return fee
    }

    changeGroupPricing(event: any) {

        let fee = this.findFeeById(this.groupForm.value.feeId)
        if (!fee) {
            return
        }
        let data = this.selection.selected
        switch (this.groupForm.value.group) {
            case "GROUP":
                let items = []
                for (let i = 0; i < data.length; i++) {
                    if (!data[i].feeId) {
                        // Remove selection
                        let item = this.selection.selected[i]
                        items.push(item.id)
                    }
                }
                let dataItems = {
                    items: items,
                    feeId: fee,
                    group: this.groupForm.value.group
                }
                this.targetPricingSelection.push(dataItems)
                break
            default:
                for (let i = 0; i < data.length; i++) {
                    if (!data[i].feeId) {
                        // Remove selection
                        let item = this.selection.selected[i]
                        item.feeId = fee
                        this.targetPricingSelection.push(item)
                    }
                }
                break
        }
        this.selection.clear()
        this.groupFeeApplicable = false
        // Recalculate charges
        this.invalidFeeSelection = this.checkPricingSelected()
        if (!this.invalidFeeSelection) {
            this.saveRecord(true)
        }
        console.log("Selection changes: " + this.invalidFeeSelection)
    }

    changePricing(row: any, event: any) {
        console.log(event)
        let fee = this.findFeeById(event.value)
        if (!fee) {
            return
        }
        let rowNew = Object.assign({}, row)
        rowNew.feeId = fee
        this.targetPricingSelection.push(rowNew)
        this.selection.clear()
        this.invalidFeeSelection = this.checkPricingSelected()
        this.groupFeeApplicable = this.checkGroupPricingApplicable()
        if (!this.invalidFeeSelection) {
            this.saveRecord(true)
        }
        console.log("Selection changes: " + this.invalidFeeSelection)
    }

    isAllSelected() {
        const numSelected = this.selection.selected.length;
        const numRows = this.selectionDataSource.data.length;
        return numSelected == numRows;
    }

    /** Selects all rows if they are not all selected; otherwise clear selection. */
    masterToggle() {
        this.isAllSelected() ?
            this.selection.clear() :
            this.selectionDataSource.data.forEach(row => this.selection.select(row));
        // Check fee applicable
        this.groupFeeApplicable = this.checkGroupPricingApplicable()
    }

    saveRecord(presentment: Boolean) {
        this.saveDisabled = true
        this.message = null
        let selectedItems = this.getItems()
        if (this.items.length > 0 && selectedItems.length == 0) {
            this.saveDisabled = false
            this.itemPricingDataSource.connect().next([])
            return
        }
        let data = this.form.value
        if (presentment) {
            data["remarks"] = "Presenting"
            try {
                if (this.presentmentRequest) {
                    this.presentmentRequest.unsubscribe();
                }
            } catch (e) {
            }
        }
        data["includeAll"] = false
        data["presentment"] = presentment
        data["amount"] = this.items.length == 0 ? 0.0 : parseFloat(this.form.value.amount)
        data["items"] = selectedItems
        this.presentmentRequest = this.diService.sendDemandNote(data, this.data.uuid)
            .subscribe(
                res => {
                    this.saveDisabled = false
                    if (res.responseCode == "00") {
                        if (presentment) {
                            this.presentmentData = res.data
                            let items = this.presentmentData.items
                            // console.log(items)
                            if (items) {
                                items.push({
                                    feeName: 'Total Amount',
                                    cfvalue: this.presentmentData.demandNote.cfvalue,
                                    adjustedAmount: this.presentmentData.demandNote.totalAmount,
                                })
                            }
                            this.itemPricingDataSource.connect().next(items)
                        } else {
                            this.diService.showSuccess(res.message, () => {
                                this.dialogRef.close(res.data)
                            })
                        }
                    } else {
                        this.message = res.message
                    }
                },
                error => {
                    this.saveDisabled = false
                }
            )
    }
}
