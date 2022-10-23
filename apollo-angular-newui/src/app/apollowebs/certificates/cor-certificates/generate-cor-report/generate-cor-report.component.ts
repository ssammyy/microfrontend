import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: 'app-generate-cor-report',
  templateUrl: './generate-cor-report.component.html',
  styleUrls: ['./generate-cor-report.component.css']
})
export class GenerateCorReportComponent implements OnInit {

  public form: FormGroup;
  message: any;
  loading = false

  constructor(public dialogRef: MatDialogRef<any>, private fb: FormBuilder, @Inject(MAT_DIALOG_DATA) public data: any) {
  }

  ngOnInit(): void {
    this.form = this.fb.group({
      stationId: [null],
      startDate: [null],
      endDate: [null],
      exporter: [null],
      country: [null],
      chassis: [null]
    })
  }

  saveRecord() {
    this.dialogRef.close(this.form.value)
  }

}
