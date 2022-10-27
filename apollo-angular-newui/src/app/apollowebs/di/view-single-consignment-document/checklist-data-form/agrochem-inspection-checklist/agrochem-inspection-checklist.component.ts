import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";

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

    constructor(private fb: FormBuilder) {
    }

    ngOnInit(): void {
        this.valid.emit(true)
        this.agrochemChecklist = this.fb.group({
            remarks: ['AgroChemical Checklist'],
            items: []
        })
        this.agrochemChecklist.valueChanges
            .subscribe(()=> {
                    if (this.selectedItems && this.selectedItems.length>0) {
                        let data=this.agrochemChecklist.value
                        let v=this.validateItems(data)
                        // At least one item selected for data to be emitted.
                        this.valid.emit(v)
                        if (v) {
                            console.log("valid value changes")
                            this.agrochemDetails.emit(data)
                        } else {
                            console.log("invalid value changes: "+v)
                           this.agrochemDetails.emit(null)
                        }
                    } else {
                        this.valid.emit(true)
                        this.agrochemDetails.emit(null)
                    }
                }
            )
        // Simulate updated for value changed event
        this.agrochemChecklist.patchValue({
            remarks: ''
        })
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
