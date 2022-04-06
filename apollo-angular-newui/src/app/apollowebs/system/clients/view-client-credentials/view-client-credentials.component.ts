import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {FormBuilder, FormGroup} from "@angular/forms";

@Component({
  selector: 'app-view-client-credentials',
  templateUrl: './view-client-credentials.component.html',
  styleUrls: ['./view-client-credentials.component.css']
})
export class ViewClientCredentialsComponent implements OnInit {
  message: any
  form: FormGroup
  constructor(private fb: FormBuilder, @Inject(MAT_DIALOG_DATA) public data: any, private dialogRef: MatDialogRef<any>) { }

  ngOnInit(): void {
    this.form=this.fb.group({
      clientId: [this.data.clientId],
      clientSecret: [this.data.clientSecret]
    })
  }

}
