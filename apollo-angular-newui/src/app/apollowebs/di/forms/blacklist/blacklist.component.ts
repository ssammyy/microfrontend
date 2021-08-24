import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material/dialog";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import {DestinationInspectionService} from "../../../../core/store/data/di/destination-inspection.service";

@Component({
  selector: 'app-blacklist',
  templateUrl: './blacklist.component.html',
  styleUrls: ['./blacklist.component.css']
})
export class BlacklistComponent implements OnInit {

  blacklists: any = []
  public form: FormGroup;
  message: any;
  constructor(public dialogRef: MatDialogRef<any>, private fb: FormBuilder, @Inject(MAT_DIALOG_DATA) public data: any,
              private diService: DestinationInspectionService) {
  }

  ngOnInit(): void {
    this.loadBlackLists()
    this.form = this.fb.group({
      blacklistStatus: ['', Validators.required],
      blacklistId:  ['', Validators.required],
      blacklistRemarks: ['', Validators.required]
    })
  }

  loadBlackLists() {
    this.diService.userBlacklistTypes()
        .subscribe(
            res=>{
              if(res.responseCode==="00"){
                this.blacklists=res.data
              }else {
                this.message=res.message
              }
            }
        )
  }
 
  saveRecord() {
    this.diService.sendConsignmentDocumentAction(this.form.value,this.data.uuid,"blacklist")
    this.dialogRef.close(this.form.value)
  }
}
