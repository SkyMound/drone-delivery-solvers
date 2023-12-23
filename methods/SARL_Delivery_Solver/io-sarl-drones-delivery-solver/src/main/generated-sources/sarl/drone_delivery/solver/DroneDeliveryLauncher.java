/**
 * $Id$
 * 
 * SARL is an general-purpose agent programming language.
 * More details on http://www.sarl.io
 * 
 * Copyright (C) 2014-2023 SARL.io, the Original Authors and Main Authors
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package drone_delivery.solver;

import io.sarl.lang.core.annotation.SarlElementType;
import io.sarl.lang.core.annotation.SarlSpecification;
import io.sarl.lang.core.annotation.SyntheticMember;
import java.awt.Color;

/**
 * The main class configuring the various boids populations and launching the simulation
 * 
 * @author <a href="http://www.ciad-lab.fr/nicolas_gaud">Nicolas Gaud</a>
 */
@SarlSpecification("0.13")
@SarlElementType(10)
@SuppressWarnings("all")
public class DroneDeliveryLauncher {
  /**
   * @param args command line arguments
   */
  public static void main(final String... args) {
    BoidsSimulation simu = new BoidsSimulation();
    Population pRed = new Population(Color.RED);
    Population pGreen = new Population(Color.GREEN);
    Population pBlue = new Population(Color.BLUE);
    for (int i = 0; (i < pRed.popSize); i++) {
      simu.addBoid(pRed);
    }
    for (int i = 0; (i < pGreen.popSize); i++) {
      simu.addBoid(pGreen);
    }
    for (int i = 0; (i < pBlue.popSize); i++) {
      simu.addBoid(pBlue);
    }
    simu.start();
  }

  @SyntheticMember
  public DroneDeliveryLauncher() {
    super();
  }
}
