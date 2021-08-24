import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material/dialog";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";

@Component({
  selector: 'app-send-demand-note-tokws',
  templateUrl: './send-demand-note-tokws.component.html',
  styleUrls: ['./send-demand-note-tokws.component.css']
})
export class SendDemandNoteTokwsComponent implements OnInit {

 
  public form: FormGroup;
  constructor(public dialogRef: MatDialogRef<any>, private fb: FormBuilder, @Inject(MAT_DIALOG_DATA) public data: any) {
  }

  ngOnInit(): void {
    this.form = this.fb.group({
      sendDemandNoteRemarks: ['', Validators.required]
    })
  }
 
  saveRecord() {

    this.dialogRef.close(this.form.value)
  }
}
