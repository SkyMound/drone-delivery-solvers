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

/**
 * General configuration of the boids simulation.
 * 
 * @author <a href="http://www.ciad-lab.fr/nicolas_gaud">Nicolas Gaud</a>
 */
@SarlSpecification("0.13")
@SarlElementType(11)
@SuppressWarnings("all")
public interface Settings {
  /**
   * Environmental grid default width
   */
  static final int EnvtWidth = 800;

  /**
   * Environmental grid default height
   */
  static final int EnvtHeight = 600;

  /**
   * Boolean specifying whether message logs are activated or not
   */
  static final boolean isLogActivated = false;

  /**
   * Specify a pause delay before each boids sends his influence to the environment, and respectively before the environment sends perceptions to boids
   */
  static final int pause = 0;
}
