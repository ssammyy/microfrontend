import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: 'app-generate-general-report',
  templateUrl: './generate-general-report.component.html',
  styleUrls: ['./generate-general-report.component.css']
})
export class GenerateGeneralReportComponent implements OnInit {

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
      importer: [null],
      country: [null],
      hs_code: [null],
      description: [null]
    })
  }

  saveRecord() {
    this.dialogRef.close(this.form.value)
  }

}
