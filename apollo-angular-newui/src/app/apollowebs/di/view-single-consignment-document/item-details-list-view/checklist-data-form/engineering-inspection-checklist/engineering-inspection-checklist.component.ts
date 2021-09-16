import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MatTableDataSource} from "@angular/material/table";

@Component({
    selector: 'app-engineering-inspection-checklist',
    templateUrl: './engineering-inspection-checklist.component.html',
    styleUrls: ['./engineering-inspection-checklist.component.css']
})
export class EngineeringInspectionChecklistComponent implements OnInit {
    @Output() private engineeringDetails = new EventEmitter<any>();
    @Input() items:[]
    engineeringChecklist: FormGroup
    initialSelection: any[]
    selectionDataSource: MatTableDataSource<any>

    constructor(private fb: FormBuilder) {
    }

    ngOnInit(): void {

        this.engineeringChecklist = this.fb.group({
            serialNumber: ['', [Validators.required, Validators.maxLength(256)]],
            remarks: ['', Validators.required]
        })

        this.engineeringChecklist.statusChanges
            .subscribe(
                res=>{
                    if(this.engineeringChecklist.valid){
                        this.engineeringDetails.emit(this.engineeringChecklist.value)
                    } else {
                        this.engineeringDetails.emit(null)
                    }
                }
            )
    }

}
