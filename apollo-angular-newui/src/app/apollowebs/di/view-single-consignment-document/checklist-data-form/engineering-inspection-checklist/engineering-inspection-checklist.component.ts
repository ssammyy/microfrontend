import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MatTableDataSource} from "@angular/material/table";

@Component({
    selector: 'app-engineering-inspection-checklist',
    templateUrl: './engineering-inspection-checklist.component.html',
    styleUrls: ['./engineering-inspection-checklist.component.css']
})
export class EngineeringInspectionChecklistComponent implements OnInit {
    @Output() private engineeringDetails = new EventEmitter<any>();
    @Input() categories: any[]
    @Input() itemList: any[]
    @Output() valid=new EventEmitter<Boolean>()
    engineeringChecklist: FormGroup
    selectedItems: any[]
    selectionDataSource: MatTableDataSource<any>

    constructor(private fb: FormBuilder) {
    }

    ngOnInit(): void {
        this.valid.emit(true)
        this.engineeringChecklist = this.fb.group({
            remarks: ['Engineering checklist'],
            items: []
        })

        this.engineeringChecklist.statusChanges
            .subscribe(
                valid => {
                    if (this.selectedItems && this.selectedItems.length>0) {
                        let data=this.engineeringChecklist.value
                        let v=this.validateItems(data)
                        this.valid.emit(v)
                        // At least one engineering item to be selected
                        if (this.valid) {
                            this.engineeringDetails.emit(data)
                        } else {
                            this.engineeringDetails.emit(null)
                        }
                    } else {
                        this.valid.emit(true)
                        this.engineeringDetails.emit(null)
                    }
                }
            )
    }

    itemsSelected(items: any) {
        this.engineeringChecklist.patchValue({
            items: items
        })
        this.selectedItems = items
    }

    validateItems(data: any) {
        let validItems = true;
        if (data.items && data.items.length > 0) {
            for (let itm of data.items) {
                if (!itm.checklist) {
                    itm.valid = false
                    console.log("Invalid item: " + itm.id)
                    validItems = false
                }
            }
        }
        return validItems
    }

}
