import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MatDialog, MatDialogRef} from "@angular/material/dialog";
import {ItemChecklistComponent} from "./item-checklist/item-checklist.component";

@Component({
    selector: 'app-agrochem-inspection-checklist',
    templateUrl: './agrochem-inspection-checklist.component.html',
    styleUrls: ['./agrochem-inspection-checklist.component.css']
})
export class AgrochemInspectionChecklistComponent implements OnInit {
    @Output() private agrochemDetails = new EventEmitter<any>();
    agrochemChecklist: FormGroup
    @Input() categories: any[]
    @Input() itemList: any[]
    @Output() valid=new EventEmitter<Boolean>()
    selectedItems: []

    constructor(private fb: FormBuilder, private dialog: MatDialog) {
    }

    ngOnInit(): void {
        this.valid.emit(true)
        this.agrochemChecklist = this.fb.group({
            remarks: ['AgroChemical Checklist', Validators.required],
            items: []
        })
        this.agrochemChecklist.valueChanges
            .subscribe(
                res => {
                    if (this.agrochemChecklist.valid) {
                        console.log("valid value changes")
                        let data = this.agrochemChecklist.value
                        let v=this.validateItems(data)
                        this.valid.emit(v)
                        if (this.valid) {
                            this.agrochemDetails.emit(data)
                        } else {
                           this.agrochemDetails.emit(null)
                        }
                    } else {
                        this.valid.emit(false)
                        this.agrochemDetails.emit(null)
                    }
                }
            )
    }

    itemsSelected(items: any) {
        this.agrochemChecklist.patchValue({
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
