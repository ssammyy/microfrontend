import {Component, Input, OnInit} from '@angular/core';
import {DestinationInspectionService} from "../../../../core/store/data/di/destination-inspection.service";

@Component({
    selector: 'app-cor-fragment',
    templateUrl: './cor-fragment.component.html',
    styleUrls: ['./cor-fragment.component.css']
})
export class CorFragmentComponent implements OnInit {
    @Input()
    corDetails: any
    active: any = '0'

    constructor(private  diService: DestinationInspectionService) {
    }

    ngOnInit(): void {
    }

    createQuery() {

    }

    downloadCorFile(): void {
        if (this.corDetails) {
            this.diService.downloadDocument("/api/v1/download/cor/" + this.corDetails.cor_details.id)
        }
    }

}
