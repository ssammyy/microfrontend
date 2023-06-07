import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";

@Component({
    selector: 'app-other-inspection-checklist',
    templateUrl: './other-inspection-checklist.component.html',
    styleUrls: ['./other-inspection-checklist.component.css']
})
export class OtherInspectionChecklistComponent implements OnInit {
    @Output() private otherDetails = new EventEmitter<any>();
    @Input() categories: any[]
    @Input() itemList: any[]
    @Output() valid = new EventEmitter<Boolean>()
    selectedItems: any[]
    otherItemChecklist: FormGroup

    constructor(private fb: FormBuilder) {
    }

    ngOnInit(): void {
        this.valid.emit(true)
        this.otherItemChecklist = this.fb.group({
            remarks: ['Other checklist'],
            items: []
        })

        this.otherItemChecklist.statusChanges
            .subscribe(
                valid => {
                    if (this.selectedItems && this.selectedItems.length>0) {
                        let data=this.otherItemChecklist.value
                        let v = this.validateItems(data)
                        this.valid.emit(v)
                        // At least one other items selected
                        if (v) {
                            this.otherDetails.emit(data)
                        } else {
                            this.otherDetails.emit(null)
                        }
                    } else {
                        this.valid.emit(true)
                        this.otherDetails.emit(null)
                    }
                }
            )
    }

    itemsSelected(items: any) {
        this.otherItemChecklist.patchValue({
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
