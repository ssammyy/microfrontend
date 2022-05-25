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
    displayedColumns = ['select', 'checklist','hsCode', 'details', 'price', 'description', 'actions']
    complianceStatus = [
        {
            name: 'COMPLIANT',
            description: 'Compliant',
        },
        {
            name: 'NON-COMPLIANT',
            description: 'Non-Compliant',
        }
    ]
    engineeringSections = [
        {
            name: 'TEXTILE',
            description: 'Textile Industry',
        },
        {
            name: 'MECHANICAL',
            description: 'Mechanical Industry',
        },
        {
            name: 'CIVIL',
            description: 'Civil Industry',
        },
        {
            name: 'ELECTRICAL',
            description: 'Electrical Industry',
        },
        {
            name: 'OTHER',
            description: 'Other Industry',
        },
    ]
    agrochemicalSections = [
        {
            name: 'FOOD',
            description: 'Food Industry',
        },
        {
            name: 'AGRICULTURAL',
            description: 'Agriculture',
        },
        {
            name: 'CHEMICAL',
            description: 'Chemical Industry',
        },
        {
            name: 'OTHER',
            description: 'Other Industry',
        },
    ]
    @Output() private selectedItemsChange = new EventEmitter<any>();
    @Input() items: any[]
    @Input() categories: any[]
    @Input() selectedItems: any[]
    @Input() checklistType: any
    @Input() stations: any[]
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

    getSelectedIndex(row): number {
        for (let i = 0; i < this.items.length; i++) {
            if (row.id !== this.items[i].id) {
                continue
            }
            return i
        }
        return -1
    }

    selectionChanged(event, row) {
        let i = this.getSelectedIndex(row)
        if (!this.items[i].selected || this.items[i].selected && this.items[i].selected == this.checklistType) {
            if (event.checked) {
                this.items[i].selected = this.checklistType
                this.selection.select(row)
            } else {
                this.items[i].selected = false
                this.selection.deselect(row)
            }
        } else {
            this.selection.deselect(row)
            console.log("Item already selected")
        }
    }

    openChekclistForm(row: any) {
        let dialogRef: MatDialogRef<any> = null;
        let formData = {
            itemId: row.id,
            engineeringSections: this.engineeringSections,
            complianceStatus: this.complianceStatus,
            agrochemicalSections: this.agrochemicalSections,
            categories: this.categories,
            checklist: row.checklist
        }
        switch (this.checklistType) {
            case 'agrochem':
                dialogRef = this.dialog.open(ItemChecklistComponent, {
                    data: formData
                })
                break
            case 'engineering':
                dialogRef = this.dialog.open(EngineeringItemChecklistComponent, {
                    data: formData
                })
                break
            case 'vehicle':
                formData["stations"] = this.stations
                dialogRef = this.dialog.open(VehicleItemChecklistComponent, {
                    data: formData
                })
                break
            case 'other':
                dialogRef = this.dialog.open(OtherItemChecklistComponent, {
                    data: formData
                })
                break
            default:
                return
        }
        dialogRef.afterClosed()
            .subscribe(res => {
                    if (res) {
                        row.checklist = res
                        this.selectedItemsChange.emit(this.selection.selected)
                    }
                }
            )
    }

    masterToggle(event: any) {
            if(!event.checked) {
                this.selection.clear()
            }
            this.selectionDataSource.data.forEach(row => {
                let i=this.getSelectedIndex(row)
                // Toggle only if you own the item or can own
                if(!this.items[i].selected || this.items[i].selected && this.items[i].selected==this.checklistType) {
                    if(event.checked) {
                        this.items[i].selected=this.checklistType
                        this.selection.select(row)
                    }else {
                        this.selection.deselect(row)
                        this.items[i].selected=false
                    }
                }
            });
    }

    changeCompliance(row, event) {

    }

}
