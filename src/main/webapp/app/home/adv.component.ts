import { Component, OnInit } from '@angular/core';

@Component({
    selector: 'adv-comp',
    templateUrl: './adv.component.html',
    styleUrls: [
        'home.scss'
    ]

})
export class AdvComponent {
 advertisements: String[];


    constructor() {
        this.advertisements = [
            "http://www.stealinghomepress.com/wp-content/uploads/2017/10/Play-School-Brochure-Templates-3.jpg","http://www.stealinghomepress.com/wp-content/uploads/2017/10/Play-School-Brochure-Templates-3.jpg"
        ];
    }
}
