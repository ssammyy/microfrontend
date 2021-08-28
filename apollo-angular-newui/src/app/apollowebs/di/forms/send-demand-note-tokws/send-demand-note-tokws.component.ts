import {Component, Inject, Input, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {CdkDragDrop, moveItemInArray, transferArrayItem} from "@angular/cdk/drag-drop";

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
    public form: FormGroup;

    constructor(public dialogRef: MatDialogRef<any>, private fb: FormBuilder, @Inject(MAT_DIALOG_DATA) public data: any) {
    }

    ngOnInit(): void {
        this.form = this.fb.group({
            sendDemandNoteRemarks: ['', Validators.required],
            includeAll: [false, Validators.required]
        })
        this.selectedItems = []
        this.items = this.data.items
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

    saveRecord() {

        this.dialogRef.close(this.form.value)
    }
}
