/*
 * Copyright 2000-2014 JetBrains s.r.o.
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
package com.intellij.ide.scratch;

import com.intellij.lang.Language;
import com.intellij.lang.PerFileMappings;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.containers.ContainerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public abstract class ScratchFileService {

  public static RootType SCRATCHES = RootType.newRootType("scratches", "Scratches");

  public static ScratchFileService getInstance(@NotNull Project project) {
    return ServiceManager.getService(project, ScratchFileService.class);
  }

  public static ScratchFileService getInstance() {
    return ServiceManager.getService(ScratchFileService.class);
  }

  @NotNull
  public abstract String getRootPath(@NotNull RootType rootType);

  @Nullable
  public abstract VirtualFile createScratchFile(@NotNull Project project, @NotNull Language language, @NotNull String initialContent);

  public abstract boolean isScratchFile(@NotNull VirtualFile file);

  @NotNull
  public abstract PerFileMappings<Language> getScratchesMapping();

  public static class RootType {
    private static final Map<String, RootType> ourInstances = ContainerUtil.newLinkedHashMap();

    private final String myId;
    private final String myDisplayName;

    private RootType(String id, String displayName) {
      myId = id;
      myDisplayName = displayName;
    }

    public String getId() {
      return myId;
    }

    public String getDisplayName() {
      return myDisplayName;
    }

    public boolean isHidden() {
      return myDisplayName == null;
    }

    public static synchronized RootType newRootType(String id, String displayName) {
      RootType rootType = new RootType(id, displayName);
      RootType prev = ourInstances.put(id, rootType);
      if (prev != null) {
        throw new AssertionError(String.format("rootType '%s' already registered", id));
      }
      return rootType;
    }

    public static synchronized List<RootType> getAllRootTypes() {
      return ContainerUtil.newArrayList(ourInstances.values());
    }
  }
}
