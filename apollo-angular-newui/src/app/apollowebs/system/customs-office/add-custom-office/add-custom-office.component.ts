import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {DestinationInspectionService} from "../../../../core/store/data/di/destination-inspection.service";

@Component({
  selector: 'app-add-custom-office',
  templateUrl: './add-custom-office.component.html',
  styleUrls: ['./add-custom-office.component.css']
})
export class AddCustomOfficeComponent implements OnInit {
  laboratoryStatuses = [
    {
      name: '1',
      description: 'Active'
    },
    {
      name: '0',
      description: 'Disabled'
    }
  ]
  form: FormGroup
  message: any
  loading = false

  constructor(private fb: FormBuilder, private dialogRef: MatDialogRef<any>, @Inject(MAT_DIALOG_DATA) public  data: any, private diService: DestinationInspectionService) {
  }

  ngOnInit(): void {

    this.form = this.fb.group({
      customsOfficeName: [this.data ? this.data.customsOfficeName : '', [Validators.required, Validators.minLength(2)]],
      customsOfficeCode: [this.data ? this.data.customsOfficeCode : '', [Validators.required]],
      description: [this.data ? this.data.description : '', [Validators.required]],
      status: [this.data ? this.data.status : '', [Validators.required]]
    })
  }

  addLaboratory() {
    this.loading = true
    let call = this.diService.saveCustomOffice(this.form.value)
    if (this.data) {
      call = this.diService.updateCustomOffice(this.form.value, this.data.id)
    }
    call.subscribe(
        res => {
          if (res.responseCode === "00") {
            this.dialogRef.close(true)
          } else {
            this.message = res.message;
          }
          this.loading = false
        },
        error => {
          console.log(error)
          this.message = "Failed to send message"
          this.loading = false
        }
    )
  }

}
