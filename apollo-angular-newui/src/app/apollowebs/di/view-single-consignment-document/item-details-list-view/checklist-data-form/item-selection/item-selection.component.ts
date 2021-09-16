import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {MatTableDataSource} from "@angular/material/table";
import {SelectionModel} from "@angular/cdk/collections";
import {MatDialog, MatDialogRef} from "@angular/material/dialog";
import {ItemChecklistComponent} from "../agrochem-inspection-checklist/item-checklist/item-checklist.component";
import {EngineeringItemChecklistComponent} from "../engineering-inspection-checklist/engineering-item-checklist/engineering-item-checklist.component";
import {VehicleItemChecklistComponent} from "../vehicle-inspection-checklist/vehicle-item-checklist/vehicle-item-checklist.component";
import {OtherItemChecklistComponent} from "../other-inspection-checklist/other-item-checklist/other-item-checklist.component";

@Component({
    selector: 'app-item-selection',
    templateUrl: './item-selection.component.html',
    styleUrls: ['./item-selection.component.css']
})
export class ItemSelectionComponent implements OnInit {
    displayedColumns = ['select', 'hsCode', 'details', 'price', 'description', 'actions']
    complianceStatus = [
        {
            name: 'COMPLIANCE',
            description: 'Compliant',
        },
        {
            name: 'NON-COMPLIANCE',
            description: 'Non-Compliant',
        }
    ]
    @Output() private selectedItemsChange = new EventEmitter<any>();
    @Input() items: any[]
    @Input() categories: any[]
    @Input() selectedItems: any[]
    @Input() checklistType: any
    selectionDataSource: MatTableDataSource<any>
    selection: SelectionModel<any>
    invalidFeeSelection: Boolean;

    constructor(private dialog: MatDialog) {
    }

    ngOnInit(): void {
        this.selectionDataSource = new MatTableDataSource<any>(this.items)
        this.invalidFeeSelection = true
        this.selection = new SelectionModel<any>(true, this.selectedItems);
        this.selection.changed
            .subscribe(
                res => {
                    console.log("Selection changes: " + res)
                    this.invalidFeeSelection = this.selection.isEmpty()
                    this.selectedItemsChange.emit(this.selection.selected)
                }
            )
    }

    isAllSelected() {
        const numSelected = this.selection.selected.length;
        const numRows = this.selectionDataSource.data.length;
        return numSelected == numRows;
    }

    selectionChanged(event, row) {
        if(event.checked) {
            this.selection.select(row)
        }else {
            this.selection.deselect(row)
        }
    }

    openChekclistForm(row: any) {
        let dialogRef: MatDialogRef<any> = null;
        let formData={
            itemId: row.id,
            complianceStatus: this.complianceStatus,
            categories: this.categories,
            checklist: row.checklist
        }
        switch (this.checklistType) {
            case 'agrochem':
                dialogRef = this.dialog.open(ItemChecklistComponent,{
                    data : formData
                })
                break
            case 'engineering':
                dialogRef = this.dialog.open(EngineeringItemChecklistComponent,{
                    data : formData
                })
                break
            case 'vehicle':
                dialogRef = this.dialog.open(VehicleItemChecklistComponent,{
                    data : formData
                })
                break
            case 'other':
                dialogRef = this.dialog.open(OtherItemChecklistComponent,{
                    data : formData
                })
                break
            default:
                return
        }
        dialogRef.afterClosed()
            .subscribe(res => {
                    if (res) {
                        row.checklist=res
                        this.selectedItemsChange.emit(this.selection.selected)
                    }
                }
            )
    }

    masterToggle() {
        this.isAllSelected() ?
            this.selection.clear() :
            this.selectionDataSource.data.forEach(row => this.selection.select(row));
    }

    changeCompliance(row, event) {

    }

}
