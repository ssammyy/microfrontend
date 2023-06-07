import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {DestinationInspectionService} from "../../../../core/store/data/di/destination-inspection.service";

@Component({
    selector: 'app-assign-port',
    templateUrl: './assign-port.component.html',
    styleUrls: ['./assign-port.component.css']
})
export class AssignPortComponent implements OnInit {
    public form: FormGroup;
    public ports: any = [];
    public clusters: any[] = []
    public freightStations: any[] = []
    loading: boolean = false
    message: string;
    sections: any

    constructor(public dialogRef: MatDialogRef<any>, private fb: FormBuilder, @Inject(MAT_DIALOG_DATA) public data: any,
                private diService: DestinationInspectionService) {
    }

    ngOnInit(): void {
        this.sections = {}
        this.loadPorts()
        this.form = this.fb.group({
            portOfArrival: ['', Validators.required],
            clusterId: [''],
            freightStation: ['',],
            assignPortRemarks: ['', Validators.required]
        })
    }

    loadSubSections(event: any) {
        let portId = event.target.value
        let section = this.sections[portId];
        if (section) {
            this.freightStations = section
        } else {
            this.diService.getAllPortFreightStations(portId)
                .subscribe(
                    res => {
                        if (res.responseCode === "00") {
                            this.freightStations = res.data
                            // Cache result
                            this.sections[portId] = this.freightStations
                        } else {
                            this.freightStations = []
                            console.log(res.message)
                        }
                    }
                )
        }
    }

    loadPorts() {
        this.diService.getAllPorts()
            .subscribe(
                res => {
                    if (res.responseCode === "00") {
                        this.ports = res.data
                    } else {
                        console.log(res.message)
                    }
                }
            )

    }

    saveRecord() {
        this.loading = true
        this.diService.assignPort(this.form.value, this.data.uuid)
            .subscribe(res => {
                    this.loading = false
                    if (res.responseCode === "00") {
                        this.diService.showSuccess(res.message, () => {
                            this.dialogRef.close(true)
                        })
                    } else {
                        this.message = res.message
                    }
                },
                error => {
                    this.loading = false
                })
    }
}