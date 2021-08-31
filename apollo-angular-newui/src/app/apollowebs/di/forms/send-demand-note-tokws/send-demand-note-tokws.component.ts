import {Component, Inject, Input, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {CdkDragDrop, moveItemInArray, transferArrayItem} from "@angular/cdk/drag-drop";
import {DestinationInspectionService} from "../../../../core/store/data/di/destination-inspection.service";

@Component({
    selector: 'app-send-demand-note-tokws',
    templateUrl: './send-demand-note-tokws.component.html',
    styleUrls: ['./send-demand-note-tokws.component.css']
})
export class SendDemandNoteTokwsComponent implements OnInit {
    buckets: any[] = [
        {id: 'Items', desc: 'List of items'},
        {id: 'Included', desc: 'done', items: []}];
    @Input() items: any[]
    selectedItems: any[]
    presentmentData: any
    message: any
    public form: FormGroup;

    constructor(public dialogRef: MatDialogRef<any>, private fb: FormBuilder, @Inject(MAT_DIALOG_DATA) public data: any,
                private diService: DestinationInspectionService) {
    }

    ngOnInit(): void {
        this.form = this.fb.group({
            remarks: ['', Validators.required],
            includeAll: [false, Validators.required]
        })
        this.selectedItems = []
        this.items = this.data.items
    }

    getItems() {
        let itemList = []
        for (let i of this.selectedItems) {
            itemList.push({
                "itemId": i.id,
                "feeId": i.feeId,
            })
        }
        return itemList
    }

    drop(event: CdkDragDrop<any[]>) {
        if (event.previousContainer === event.container) {
            moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);
        } else {
            transferArrayItem(event.previousContainer.data,
                event.container.data,
                event.previousIndex,
                event.currentIndex);
        }
    }

    saveRecord(presentment: Boolean) {
        let data = this.form.value
        data["presentment"] = presentment
        data["items"] = this.getItems()
        this.diService.sendDemandNote(data, this.data.cdUuud)
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
