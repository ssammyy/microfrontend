import {Component, Inject, Input, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {CdkDragDrop, moveItemInArray, transferArrayItem} from "@angular/cdk/drag-drop";
import {DestinationInspectionService} from "../../../../core/store/data/di/destination-inspection.service";
import {DataSource, SelectionModel} from "@angular/cdk/collections";
import {LocalDataSource} from "ng2-smart-table";
import {MatTableDataSource} from "@angular/material/table";

@Component({
    selector: 'app-send-demand-note-tokws',
    templateUrl: './send-demand-note-tokws.component.html',
    styleUrls: ['./send-demand-note-tokws.component.css']
})
export class SendDemandNoteTokwsComponent implements OnInit {
    displayedColumns: string[] = ['select', 'hsCode', 'description','price', 'pricing'];
    @Input() items: any[]
    paymentFees: any[]
    presentmentData: any
    message: any
    public form: FormGroup;
    initialSelection: any[]
    selectionDataSource: MatTableDataSource<any>
    selection: SelectionModel<any>
    invalidFeeSelection: Boolean;

    constructor(public dialogRef: MatDialogRef<any>, private fb: FormBuilder, @Inject(MAT_DIALOG_DATA) public data: any,
                private diService: DestinationInspectionService) {
    }

    ngOnInit(): void {
        this.form = this.fb.group({
            remarks: ['Okay', Validators.minLength(5)],
            amount: ['']
        })
        this.items = this.data.items
        this.selectionDataSource = new MatTableDataSource<any>(this.items)
        this.paymentFees = this.data.paymentFees
        this.initialSelection = this.items
        this.invalidFeeSelection = true
        this.selection = new SelectionModel<any>(true, this.initialSelection);
        this.selection.changed
            .subscribe(
                res => {
                    console.log("Selection changes: "+res)
                    this.invalidFeeSelection = this.checkPricingSelected()
                }
            )
    }

    getItems() {
        let itemList = []
        for (let i of this.selection.selected) {
            if(i.feeId) {
                itemList.push({
                    "itemId": i.id,
                    "feeId": i.feeId.id,
                })
            }
        }
        return itemList
    }

    selectionChanged(event,row){
        event ? this.selection.toggle(row) : null
        this.invalidFeeSelection=this.checkPricingSelected()
        if(!this.invalidFeeSelection){
            this.saveRecord(true)
        }
    }
    // Returns true if any item did not have a selected fee
    // Returns false if all items had fee selected
    checkPricingSelected(): Boolean {
        for (let i of this.selection.selected) {
            console.log(i.feeId)
            if (!i.feeId || !i.feeId.id || i.feeId=="None") {
                return false
            }
        }
        return false
    }

    changePricing(row: any, event: any) {
        this.invalidFeeSelection = this.checkPricingSelected()
        if(!this.invalidFeeSelection) {
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
    }

    saveRecord(presentment: Boolean) {
        this.message=null
        let selectedItems=this.getItems()
        if(this.items.length>0 && selectedItems.length==0){
            return
        }
        let data = this.form.value
        data["includeAll"] = false
        data["presentment"] = presentment
        data["amount"]=this.items.length==0?0.0:parseFloat(this.form.value.amount)
        data["items"] = selectedItems
        this.diService.sendDemandNote(data, this.data.uuid)
            .subscribe(
                res => {
                    if (res.responseCode == "00") {
                        if (presentment) {
                            this.presentmentData = res.data
                        } else {
                            this.dialogRef.close(true)
                        }
                    } else {
                        this.message = res.message
                    }
                }
            )
    }
}
