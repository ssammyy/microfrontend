import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {DestinationInspectionService} from "../../../../core/store/data/di/destination-inspection.service";

@Component({
    selector: 'app-assign-officer',
    templateUrl: './assign-officer.component.html',
    styleUrls: ['./assign-officer.component.css']
})
export class AssignOfficerComponent implements OnInit {


    public form: FormGroup;
    public officers: any;
    message: string

    constructor(public dialogRef: MatDialogRef<any>, private fb: FormBuilder, @Inject(MAT_DIALOG_DATA) public data: any,
                private diService: DestinationInspectionService) {
    }

    ngOnInit(): void {
        this.loadOfficers();
        this.officers=[]
        this.form = this.fb.group({
            assignedStatus: ['1', Validators.required],
            officerId: ['', Validators.required],
            remarks: ['', Validators.required]
        })
    }
    // Assign inspection officer to this consignment
    assignOfficer() {
        let data=this.form.value
        data["officerId"]=parseInt(this.form.value.officerId)
        this.diService.assignInspectionOfficer(data,this.data.uuid)
            .subscribe(
                res=>{
                    if(res.responseCode==="00"){
                        this.dialogRef.close(true)
                    }else{
                        this.message=res.message
                    }
                }
            )
    }
    // Load data here
    loadOfficers(): void {
        this.diService.listOfficersForConsignment(this.data.uuid)
            .subscribe(
                res => {
                    if (res.responseCode === "00") {
                        this.officers = res.data
                    } else {
                        this.message = res.message
                    }
                },
                error => {
                    console.log(error)
                }
            )
    }
}